package com.example.demo.service;

import com.example.demo.exception.CancelamentoNaoPermitidoException;
import com.example.demo.exception.InvalidDataException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.exception.QuartoOcupadoException;
import com.example.demo.exception.ReservaSobrepostaClienteException;
import com.example.demo.model.Cliente;
import com.example.demo.model.Quarto;
import com.example.demo.model.Reserva;
import com.example.demo.enums.StatusReserva;
import com.example.demo.enums.TipoPagamento;
import com.example.demo.repository.ClienteRepository;
import com.example.demo.repository.QuartoRepository;
import com.example.demo.repository.ReservaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final ClienteRepository clienteRepository;
    private final QuartoRepository quartoRepository;
    private final EmailService emailService;

    // Configuráveis (pode mover para application.properties)
    private static final BigDecimal TAXA_SERVICO_PERCENT = new BigDecimal("0.10"); // 10%
    private static final BigDecimal ACRESCIMO_CREDITO = new BigDecimal("0.05"); // 5%
    private static final long HORAS_CANCELAMENTO = 48L; // 48 horas

    public ReservaService(ReservaRepository reservaRepository,
                          ClienteRepository clienteRepository,
                          QuartoRepository quartoRepository,
                          EmailService emailService) {
        this.reservaRepository = reservaRepository;
        this.clienteRepository = clienteRepository;
        this.quartoRepository = quartoRepository;
        this.emailService = emailService;
    }

    public Reserva criarReserva(Long clienteId, Long quartoId, LocalDate dataCheckin, LocalDate dataCheckout, TipoPagamento tipoPagamento) {
        // validações básicas
        if (dataCheckin == null || dataCheckout == null || !dataCheckin.isBefore(dataCheckout)) {
            throw new InvalidDataException("Datas inválidas: checkin deve ser antes do checkout.");
        }
        if (dataCheckin.isBefore(LocalDate.now())) {
            throw new InvalidDataException("Data de checkin não pode ser no passado.");
        }

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado: " + clienteId));
        Quarto quarto = quartoRepository.findById(quartoId)
                .orElseThrow(() -> new NotFoundException("Quarto não encontrado: " + quartoId));

        // Verifica sobreposição de reservas no mesmo quarto (APENAS status ativos)
        List<StatusReserva> activeStatuses = List.of(StatusReserva.ABERTA, StatusReserva.CHECKIN);
        long conflitosQuarto = reservaRepository.countOverlappingReservationsForQuarto(quartoId, dataCheckin, dataCheckout, activeStatuses);
        if (conflitosQuarto > 0) {
            throw new QuartoOcupadoException("Quarto já reservado nesse período.");
        }

        // Verifica sobreposição de reservas para o mesmo cliente
        long conflitosCliente = reservaRepository.countOverlappingReservationsForCliente(clienteId, dataCheckin, dataCheckout, activeStatuses);
        if (conflitosCliente > 0) {
            throw new ReservaSobrepostaClienteException("Cliente já possui reserva em período que se sobrepõe.");
        }

        // cálculo de valores
        long nDiarias = ChronoUnit.DAYS.between(dataCheckin, dataCheckout);
        if (nDiarias <= 0) {
            throw new InvalidDataException("Período inválido (1 ou mais diárias esperadas).");
        }
        BigDecimal subtotal = quarto.getCustoDiario().multiply(BigDecimal.valueOf(nDiarias));
        BigDecimal valorTaxaServico = subtotal.multiply(TAXA_SERVICO_PERCENT);
        BigDecimal valorTotal = subtotal.add(valorTaxaServico);

        if (tipoPagamento == TipoPagamento.CREDITO) {
            valorTotal = valorTotal.add(valorTotal.multiply(ACRESCIMO_CREDITO));
        }

        // Cria reserva
        Reserva reserva = new Reserva();
        reserva.setCliente(cliente);
        reserva.setQuarto(quarto);
        reserva.setDataCheckin(dataCheckin);
        reserva.setDataCheckout(dataCheckout);
        reserva.setValorDiaria(quarto.getCustoDiario());
        reserva.setValorTaxaServico(valorTaxaServico);
        reserva.setTipoPagamento(tipoPagamento);
        reserva.setValorTotal(valorTotal);
        reserva.setStatus(StatusReserva.ABERTA);

        Reserva salva = reservaRepository.save(reserva);

        // Envia email (simulado)
        try {
            emailService.enviarEmailReserva(salva);
        } catch (Exception e) {
            // log e não interrompe criação — você pode guardar um flag se quiser
            System.err.println("Falha ao enviar e-mail (simulado): " + e.getMessage());
        }

        return salva;
    }

    public Reserva buscarPorId(Long id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Reserva não encontrada: " + id));
    }

    public List<Reserva> listarReservasDoCliente(Long clienteId) {
        // valida cliente existe
        if (!clienteRepository.existsById(clienteId)) {
            throw new NotFoundException("Cliente não encontrado: " + clienteId);
        }
        return reservaRepository.findByClienteIdOrderByDataCheckinDesc(clienteId);
    }

    public Reserva cancelarReserva(Long reservaId) {
        Reserva reserva = buscarPorId(reservaId);

        if (reserva.getStatus() != StatusReserva.ABERTA) {
            throw new InvalidDataException("Só é possível cancelar reservas com status ABERTA.");
        }

        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime checkinDateTime = reserva.getDataCheckin().atStartOfDay();
        long horasAntes = ChronoUnit.HOURS.between(agora, checkinDateTime);
        if (horasAntes < HORAS_CANCELAMENTO) {
            throw new CancelamentoNaoPermitidoException("Cancelamento permitido apenas com mínimo de 48 horas antes do checkin.");
        }

        reserva.setStatus(StatusReserva.CANCELADA);
        Reserva salva = reservaRepository.save(reserva);
        return salva;
    }

    public Reserva realizarCheckin(Long reservaId) {
        Reserva reserva = buscarPorId(reservaId);

        if (reserva.getStatus() != StatusReserva.ABERTA) {
            throw new InvalidDataException("Somente reservas com status ABERTA podem fazer checkin.");
        }

        // opcional: permitir checkin apenas no dia do checkin ou depois?
        // Aqui permitimos checkin se data atual estiver entre checkin e checkout (inclusive)
        LocalDate hoje = LocalDate.now();
        if (hoje.isBefore(reserva.getDataCheckin()) || hoje.isAfter(reserva.getDataCheckout().minusDays(1))) {
            // permitimos somente se estiver no período
            // se preferir mudar a regra, ajuste aqui
            throw new InvalidDataException("Checkin só permitido no período da reserva.");
        }

        reserva.setStatus(StatusReserva.CHECKIN);
        reserva.setDataHoraEntrada(LocalDateTime.now());
        return reservaRepository.save(reserva);
    }

    public Reserva realizarCheckout(Long reservaId) {
        Reserva reserva = buscarPorId(reservaId);

        if (reserva.getStatus() != StatusReserva.CHECKIN) {
            throw new InvalidDataException("Somente reservas com status CHECKIN podem fazer checkout.");
        }

        reserva.setStatus(StatusReserva.CHECKOUT);
        reserva.setDataHoraFinalizacao(LocalDateTime.now());
        return reservaRepository.save(reserva);
    }

    // Método para job agendado que marca EXPIRADA (pode ser chamado por um scheduler)
    public List<Reserva> encontrarReservasExpiradas() {
        LocalDate hoje = LocalDate.now();
        return reservaRepository.findExpiredReservations(StatusReserva.ABERTA, hoje);
    }

    public Reserva marcarComoExpirada(Long reservaId) {
        Reserva r = buscarPorId(reservaId);
        if (r.getStatus() == StatusReserva.ABERTA && r.getDataCheckin().isBefore(LocalDate.now())) {
            r.setStatus(StatusReserva.EXPIRADA);
            return reservaRepository.save(r);
        } else {
            throw new InvalidDataException("Reserva não pode ser marcada como expirada.");
        }
    }
}

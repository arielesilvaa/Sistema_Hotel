package com.example.demo.service;

import com.example.demo.dto.ReservaRequestDTO; // Adicione o DTO se for usá-lo
import com.example.demo.enums.*;
import com.example.demo.exception.*;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional
public class ReservaService {

    private static final Logger logger = LoggerFactory.getLogger(ReservaService.class);

    private final ReservaRepository reservaRepository;
    private final ClienteRepository clienteRepository;
    private final QuartoRepository quartoRepository;
    private final EmailService emailService;

    // Constantes para regras de negócio
    private static final BigDecimal TAXA_SERVICO_PERCENT = new BigDecimal("0.10");
    private static final BigDecimal ACRESCIMO_CREDITO = new BigDecimal("0.05");
    private static final long HORAS_CANCELAMENTO = 48L;
    private static final List<StatusReserva> ACTIVE_STATUSES = List.of(StatusReserva.ABERTA, StatusReserva.CHECKIN);

    public ReservaService(ReservaRepository reservaRepository,
                          ClienteRepository clienteRepository,
                          QuartoRepository quartoRepository,
                          EmailService emailService) {
        this.reservaRepository = reservaRepository;
        this.clienteRepository = clienteRepository;
        this.quartoRepository = quartoRepository;
        this.emailService = emailService;
    }

    // Método para ser chamado pelo Controller
    public Reserva criarReserva(ReservaRequestDTO dto) {
        return criarReserva(dto.getClienteId(), dto.getQuartoId(), dto.getDataCheckin(),
                dto.getDataCheckout(), dto.getTipoPagamento());
    }

    /**
     * Cria uma nova reserva, aplicando todas as regras de negócio e validações.
     */
    public Reserva criarReserva(Long clienteId, Long quartoId, LocalDate dataCheckin, LocalDate dataCheckout, TipoPagamento tipoPagamento) {

        // 1. VALIDAÇÃO DE BLOQUEIO E DATAS
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado: " + clienteId));

        // Regra de bloqueio por No-Show (utiliza o campo 'bloqueado' do Cliente)
        if (cliente.isBloqueado()) {
            throw new ClienteBloqueadoException("Cliente bloqueado. Histórico de 'No-Show' (reserva expirada) impede novas reservas.");
        }

        // Validação básica de datas
        if (dataCheckin == null || dataCheckout == null || !dataCheckin.isBefore(dataCheckout)) {
            throw new InvalidDataException("Datas inválidas: checkin deve ser antes do checkout.");
        }

        // Não permite reserva no passado (hoje é permitido)
        if (dataCheckin.isBefore(LocalDate.now())) {
            throw new InvalidDataException("Data de checkin não pode ser no passado.");
        }

        // 2. BUSCA DE ENTIDADES
        Quarto quarto = quartoRepository.findById(quartoId)
                .orElseThrow(() -> new NotFoundException("Quarto não encontrado: " + quartoId));

        // CONVERSÃO DE TIPO (CORREÇÃO CRUCIAL PARA O ERRO 500)
        // Checkin: no início do dia
        LocalDateTime checkinDateTime = dataCheckin.atStartOfDay();

        LocalDateTime checkoutDateTime = dataCheckout.atTime(23, 59, 59);

        // 3. VALIDAÇÕES DE CONFLITO (SOBREPOSIÇÃO)
        // **IMPORTANTE:** Certifique-se de que os métodos do Repository usam LocalDateTime para ambos os parâmetros

        // Verificação de conflitos para o Quarto
        long conflitosQuarto = reservaRepository.countOverlappingReservationsForQuarto(quartoId, checkinDateTime, checkoutDateTime, ACTIVE_STATUSES);
        if (conflitosQuarto > 0) {
            throw new QuartoOcupadoException("Quarto já reservado nesse período.");
        }

        // Verificação de conflitos para o Cliente
        long conflitosCliente = reservaRepository.countOverlappingReservationsForCliente(clienteId, checkinDateTime, checkoutDateTime, ACTIVE_STATUSES);
        if (conflitosCliente > 0) {
            throw new ReservaSobrepostaClienteException("Cliente já possui reserva em período que se sobrepõe.");
        }


        long nDiarias = ChronoUnit.DAYS.between(dataCheckin, dataCheckout);
        BigDecimal subtotal = quarto.getCustoDiario().multiply(BigDecimal.valueOf(nDiarias));

        // Taxa de Serviço e Acréscimo... (Lógica existente)
        BigDecimal valorTaxaServico = subtotal.multiply(TAXA_SERVICO_PERCENT);
        BigDecimal valorTotal = subtotal.add(valorTaxaServico);

        if (tipoPagamento == TipoPagamento.CREDITO) {
            valorTotal = valorTotal.add(valorTotal.multiply(ACRESCIMO_CREDITO));
        }

        // 5. CRIAÇÃO E PERSISTÊNCIA DA RESERVA

        Reserva reserva = new Reserva();
        reserva.setCliente(cliente);
        reserva.setQuarto(quarto);
        reserva.setDataCheckin(checkinDateTime);   // AGORA LocalDateTime
        reserva.setDataCheckout(checkoutDateTime); // AGORA LocalDateTime (CORRIGIDO)
        reserva.setValorDiaria(quarto.getCustoDiario());
        reserva.setValorTaxaServico(valorTaxaServico);
        reserva.setTipoPagamento(tipoPagamento);
        reserva.setValorTotal(valorTotal);
        reserva.setStatus(StatusReserva.ABERTA);

        Reserva reservaSalva = reservaRepository.save(reserva);

        // 6. AÇÃO PÓS-PERSISTÊNCIA (E-mail)
        try {
            emailService.enviarEmailReserva(reservaSalva);
        } catch (Exception e) {
            logger.error("Falha ao enviar e-mail (simulado) para reserva {}: {}", reservaSalva.getId(), e.getMessage());
        }

        return reservaSalva;
    }

    // --- Outros Métodos ---

    public Reserva cancelarReserva(Long reservaId) {
        Reserva reserva = buscarPorId(reservaId);

        if (reserva.getStatus() != StatusReserva.ABERTA)  {
            throw new InvalidDataException("Só é possível cancelar reservas com status ABERTA.");
        }

        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime checkinDateTime = reserva.getDataCheckin();
        long horasAntes = ChronoUnit.HOURS.between(agora, checkinDateTime);

        if (horasAntes < HORAS_CANCELAMENTO) {
            throw new CancelamentoNaoPermitidoException("Cancelamento permitido apenas com mínimo de 48 horas antes do checkin.");
        }

        reserva.setStatus(StatusReserva.CANCELADA);
        return reservaRepository.save(reserva);
    }

    public Reserva realizarCheckin(Long reservaId) {
        Reserva reserva = buscarPorId(reservaId);

        if (reserva.getStatus() != StatusReserva.ABERTA) {
            throw new InvalidDataException("Somente reservas com status ABERTA podem fazer checkin.");
        }

        LocalDateTime agora = LocalDateTime.now();

        // Checkin permitido se a data/hora atual for maior ou igual ao checkin agendado
        // E se for antes da data/hora de checkout agendada
        if (agora.isBefore(reserva.getDataCheckin()) || agora.isAfter(reserva.getDataCheckout())) {
            throw new InvalidDataException("Checkin só permitido no período da reserva (a partir da data/hora de checkin e antes da data/hora de checkout).");
        }

        reserva.setStatus(StatusReserva.CHECKIN);
        reserva.setDataHoraEntrada(agora);
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

    public Reserva buscarPorId(Long id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Reserva não encontrada: " + id));
    }
}
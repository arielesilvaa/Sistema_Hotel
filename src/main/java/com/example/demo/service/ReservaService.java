package com.example.demo.service;

import com.example.demo.enums.StatusReserva;
import com.example.demo.enums.TipoPagamento;
import com.example.demo.exception.*;
import com.example.demo.model.Cliente;
import com.example.demo.model.Quarto;
import com.example.demo.model.Reserva;
import com.example.demo.repository.ClienteRepository;
import com.example.demo.repository.QuartoRepository;
import com.example.demo.repository.ReservaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final ClienteRepository clienteRepository;
    private final QuartoRepository quartoRepository;

    public ReservaService(ReservaRepository reservaRepository, ClienteRepository clienteRepository, QuartoRepository quartoRepository) {
        this.reservaRepository = reservaRepository;
        this.clienteRepository = clienteRepository;
        this.quartoRepository = quartoRepository;
    }

    private static final List<StatusReserva> STATUS_ATIVOS = Arrays.asList(StatusReserva.ABERTA, StatusReserva.CHECKIN, StatusReserva.PAGA);
    private static final BigDecimal TAXA_SERVICO = new BigDecimal("0.10"); // 10%
    private static final BigDecimal TAXA_CREDITO = new BigDecimal("0.05"); // 5%

    public Reserva buscarPorId(Long id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Reserva não encontrada."));
    }

    @Transactional // garante que todas as operações dentro do método sejam trat
    public Reserva criarReserva(Long clienteId, Long quartoId, LocalDateTime dataCheckin, LocalDateTime dataCheckout, TipoPagamento tipoPagamento) {

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado."));
        Quarto quarto = quartoRepository.findById(quartoId)
                .orElseThrow(() -> new NotFoundException("Quarto não encontrado."));

        if (cliente.isBloqueado()) {
            throw new ClienteBloqueadoException("Cliente bloqueado. Histórico de 'No-Show' impede novas reservas.");
        }

        if (dataCheckin.isBefore(LocalDateTime.now())) {
            throw new InvalidDataException("Data de check-in não pode ser retroativa.");
        }

        if (!dataCheckout.isAfter(dataCheckin)) {
            throw new InvalidDataException("Data de check-out deve ser posterior à data de check-in.");
        }

        LocalDateTime checkinDateTime = LocalDateTime.from(dataCheckin);
        LocalDateTime checkoutDateTime = LocalDateTime.from(dataCheckout);

        if (reservaRepository.countOverlappingReservationsForQuarto(quartoId, checkinDateTime, checkoutDateTime, STATUS_ATIVOS) > 0) {
            throw new QuartoOcupadoException("O quarto já possui uma reserva ativa para o período selecionado.");
        }
        if (reservaRepository.countOverlappingReservationsForCliente(clienteId, checkinDateTime, checkoutDateTime, STATUS_ATIVOS) > 0) {
            throw new ClienteComReservaAtivaException("O cliente já possui uma reserva ativa ou em andamento para o período selecionado.");
        }

        long dias = ChronoUnit.DAYS.between(dataCheckin, dataCheckout);
        BigDecimal valorDiaria = quarto.getCustoDiario();
        BigDecimal valorSubTotal = valorDiaria.multiply(BigDecimal.valueOf(dias));

        BigDecimal valorTaxaServico = valorSubTotal.multiply(TAXA_SERVICO);
        BigDecimal valorTotal = valorSubTotal.add(valorTaxaServico);

        if (tipoPagamento == TipoPagamento.CREDITO) {
            BigDecimal taxaCredito = valorTotal.multiply(TAXA_CREDITO);
            valorTotal = valorTotal.add(taxaCredito);
        }

        Reserva novaReserva = new Reserva();
        novaReserva.setCliente(cliente);
        novaReserva.setQuarto(quarto);
        novaReserva.setDataCheckin(checkinDateTime);
        novaReserva.setDataCheckout(checkoutDateTime);
        novaReserva.setTipoPagamento(tipoPagamento);
        novaReserva.setValorDiaria(valorDiaria);
        novaReserva.setValorTaxaServico(valorTaxaServico);
        novaReserva.setValorTotal(valorTotal);
        novaReserva.setStatus(StatusReserva.ABERTA);

        return reservaRepository.save(novaReserva);
    }


    public Reserva simularPagamento(Long reservaId, BigDecimal valorPago) {
        Reserva reserva = buscarPorId(reservaId);

        if (reserva.getStatus() != StatusReserva.ABERTA) {
            throw new InvalidStatusException("Pagamento só pode ser processado para reservas com status ABERTA.");
        }

        // Simulação de verificação de valor
        if (valorPago.compareTo(reserva.getValorTotal()) < 0) {
            throw new InvalidDataException("O valor pago é insuficiente para cobrir o total da reserva.");
        }

        reserva.setStatus(StatusReserva.PAGA);
        reserva.setDataHoraPagamento(LocalDateTime.now());

        return reservaRepository.save(reserva);
    }

    public void cancelarReserva(Long id) {
        Reserva reserva = buscarPorId(id);

        if (reserva.getStatus() != StatusReserva.ABERTA) {
            throw new InvalidStatusException("Apenas reservas com status ABERTA podem ser canceladas.");
        }

        LocalDateTime prazoLimite = reserva.getDataCheckin().minusHours(48);
        if (LocalDateTime.now().isAfter(prazoLimite)) {
            throw new InvalidDataException("Cancelamento fora do prazo. O prazo máximo é de 48 horas antes do check-in.");
        }

        reserva.setStatus(StatusReserva.CANCELADA);
        reservaRepository.save(reserva);
    }

    public Reserva realizarCheckin(Long id) {
        Reserva reserva = buscarPorId(id);
        LocalDateTime agora = LocalDateTime.now();

        if (reserva.getStatus() != StatusReserva.PAGA) {
            throw new InvalidStatusException("Check-in só pode ser realizado em reservas com status PAGA.");
        }
        // Bloco de validação de datas
        // Verifica se a data atual está dentro do período permitido para check-in
        if (agora.isBefore(reserva.getDataCheckin())) {
            throw new InvalidDataException("Check-in antecipado não permitido.");
        }
        if (agora.isAfter(reserva.getDataCheckout())) {
            throw new InvalidDataException("Check-in não permitido. A data de check-out já foi atingida.");
        } // Fim do bloco de validação

        reserva.setStatus(StatusReserva.CHECKIN);
        reserva.setDataHoraEntrada(agora);
        return reservaRepository.save(reserva);
    }

    public Reserva realizarCheckout(Long id) {
        Reserva reserva = buscarPorId(id);

        if (reserva.getStatus() != StatusReserva.CHECKIN) {
            throw new InvalidStatusException("Check-out só pode ser realizado em reservas com status CHECKIN.");
        }

        reserva.setStatus(StatusReserva.FINALIZADA);
        reserva.setDataHoraFinalizacao(LocalDateTime.now());
        return reservaRepository.save(reserva);
    }

    public List<Reserva> buscarReservasPorCliente(Long clienteId) {
        if (!clienteRepository.existsById(clienteId)) {
            throw new NotFoundException("Cliente não encontrado.");
        }

        Cliente cliente = clienteRepository.getReferenceById(clienteId);

        return reservaRepository.findByCliente(cliente);
    }
}
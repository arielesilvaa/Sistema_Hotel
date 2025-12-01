package com.example.demo.service;

import com.example.demo.model.Pagamento;
import com.example.demo.model.Reserva;
import com.example.demo.enums.TipoPagamento;
import com.example.demo.repository.PagamentoRepository;
import com.example.demo.repository.ReservaRepository;
import com.example.demo.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;
    private final ReservaRepository reservaRepository;

    public PagamentoService(PagamentoRepository pagamentoRepository, ReservaRepository reservaRepository) {
        this.pagamentoRepository = pagamentoRepository;
        this.reservaRepository = reservaRepository;
    }

    public Pagamento registrarPagamento(Long reservaId, TipoPagamento tipo, java.math.BigDecimal valor) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new NotFoundException("Reserva não encontrada: " + reservaId));

        Pagamento pagamento = new Pagamento();
        pagamento.setReserva(reserva);
        pagamento.setTipo(tipo);
        pagamento.setValorPago(valor);
        pagamento.setDataPagamento(LocalDateTime.now());

        return pagamentoRepository.save(pagamento);
    }
}

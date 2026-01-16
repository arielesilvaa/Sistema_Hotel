package com.example.demo.service;

import com.example.demo.enums.TipoPagamento;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Pagamento;
import com.example.demo.model.Reserva;
import com.example.demo.repository.PagamentoRepository;
import com.example.demo.repository.ReservaRepository;
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


        if (valor.compareTo(reserva.getValorTotal()) < 0) {
            throw new IllegalArgumentException("O valor é insuficiente para cobrir o total da reserva."
                    + reserva.getValorTotal());
        } else if (valor.compareTo(reserva.getValorTotal()) > 0) {
            throw new IllegalArgumentException("O valor pago excede o total da reserva. valor Total: "
                    + reserva.getValorTotal());
        }

        Pagamento pagamento = new Pagamento();
        pagamento.setReserva(reserva);
        pagamento.setTipo(tipo);
        pagamento.setValorPago(valor);
        pagamento.setDataPagamento(LocalDateTime.now());

        return pagamentoRepository.save(pagamento);
    }
}

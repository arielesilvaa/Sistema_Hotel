package com.example.demo.controller;

import com.example.demo.model.Pagamento;
import com.example.demo.enums.TipoPagamento;
import com.example.demo.service.PagamentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/pagamentos")
public class PagamentoController {

    private final PagamentoService pagamentoService;

    public PagamentoController(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    @PostMapping("/reserva/{reservaId}")
    public ResponseEntity<Pagamento> registrarPagamento(@PathVariable Long reservaId, @RequestBody PagamentoRequest body) {
        Pagamento pago = pagamentoService.registrarPagamento(reservaId, body.getTipo(), body.getValor());
        return ResponseEntity.ok(pago);
    }

    // DTO
    public static class PagamentoRequest {
        private TipoPagamento tipo;
        private BigDecimal valor;

        public TipoPagamento getTipo() { return tipo; }
        public void setTipo(TipoPagamento tipo) { this.tipo = tipo; }

        public BigDecimal getValor() { return valor; }
        public void setValor(BigDecimal valor) { this.valor = valor; }
    }
}

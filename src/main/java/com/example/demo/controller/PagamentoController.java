package com.example.demo.controller;

import com.example.demo.model.Pagamento;
import com.example.demo.enums.TipoPagamento;
import com.example.demo.service.PagamentoService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController // Indica que essa classe é um controlador REST
@RequestMapping("/api/pagamentos") // Mapeia as requisições que começam com /api/pagamentos para esse controlador
public class PagamentoController {

    private final PagamentoService pagamentoService; // Dependência do serviço de pagamento

    public PagamentoController(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    } // Injeção de dependência via construtor

    @PostMapping("/{reservaId}/reserva") // Mapeia requisições POST para esse método
    public ResponseEntity<Pagamento> registrarPagamento(@PathVariable Long reservaId, @RequestBody PagamentoRequest body) {
        Pagamento pago = pagamentoService.registrarPagamento(reservaId, body.getTipo(), body.getValor());
        return ResponseEntity.ok(pago); // Retorna o pagamento registrado
    }

    // DTO é uma classe simples usada para transferir dados

    @Setter
    @Getter
    public static class PagamentoRequest {
        private TipoPagamento tipo;
        private BigDecimal valor;
    } // Classe interna para representar o corpo da requisição de pagamento
}

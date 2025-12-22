package com.example.demo.controller;

import com.example.demo.dto.APIResponse;
import com.example.demo.dto.SuccessResponse;
import com.example.demo.model.Pagamento;
import com.example.demo.enums.TipoPagamento;
import com.example.demo.service.PagamentoService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
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

    @PostMapping("/{reservaId}/reserva")
    public ResponseEntity<SuccessResponse<EntityModel<Pagamento>>> registrarPagamento(@PathVariable Long reservaId, @RequestBody PagamentoRequest body) {
        Pagamento pago = pagamentoService.registrarPagamento(reservaId, body.getTipo(), body.getValor());
        EntityModel<Pagamento> resource = EntityModel.of(pago);

        resource.add(linkTo(methodOn(ReservaController.class).buscarReservaPorId(reservaId)).withRel("ver_reserva"));

        return ResponseEntity.ok(new SuccessResponse<>(resource));
    }

    @Setter @Getter
    public static class PagamentoRequest {
        private TipoPagamento tipo;
        private BigDecimal valor;
    }
}
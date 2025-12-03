package com.example.demo.controller;

import com.example.demo.model.Reserva;
import com.example.demo.enums.TipoPagamento;
import com.example.demo.service.ReservaService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    // Criar reserva
    @PostMapping
    public ResponseEntity<Reserva> criarReserva(@Valid @RequestBody CriarReservaRequest body) {
        Reserva criada = reservaService.criarReserva(
                body.getClienteId(),
                body.getQuartoId(),
                body.getDataCheckin(),
                body.getDataCheckout(),
                body.getTipoPagamento()
        );
        return ResponseEntity.ok(criada);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reserva> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.buscarPorId(id));
    }

    // Listar reservas de um cliente
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Reserva>> listarDoCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(reservaService.listarReservasDoCliente(clienteId));
    }

    // Cancelar reserva
    @PostMapping("/{id}/cancelar")
    public ResponseEntity<Reserva> cancelar(@PathVariable Long id) {
        Reserva salva = reservaService.cancelarReserva(id);
        return ResponseEntity.ok(salva);
    }

    // Checkin
    @PostMapping("/{id}/checkin")
    public ResponseEntity<Reserva> checkin(@PathVariable Long id) {
        Reserva salva = reservaService.realizarCheckin(id);
        return ResponseEntity.ok(salva);
    }

    // Checkout
    @PostMapping("/{id}/checkout")
    public ResponseEntity<Reserva> checkout(@PathVariable Long id) {
        Reserva salva = reservaService.realizarCheckout(id);
        return ResponseEntity.ok(salva);
    }

    // DTO para criação
    public static class CriarReservaRequest {
        private Long clienteId;
        private Long quartoId;

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        private LocalDate dataCheckin;
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        private LocalDate dataCheckout;

        private TipoPagamento tipoPagamento;

        public Long getClienteId() { return clienteId; }
        public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

        public Long getQuartoId() { return quartoId; }
        public void setQuartoId(Long quartoId) { this.quartoId = quartoId; }

        public LocalDate getDataCheckin() { return dataCheckin; }
        public void setDataCheckin(LocalDate dataCheckin) { this.dataCheckin = dataCheckin; }

        public LocalDate getDataCheckout() { return dataCheckout; }
        public void setDataCheckout(LocalDate dataCheckout) { this.dataCheckout = dataCheckout; }

        public TipoPagamento getTipoPagamento() { return tipoPagamento; }
        public void setTipoPagamento(TipoPagamento tipoPagamento) { this.tipoPagamento = tipoPagamento; }
    }
}

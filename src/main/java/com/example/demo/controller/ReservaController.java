package com.example.demo.controller;

import com.example.demo.model.Reserva;
import com.example.demo.service.ReservaService;
import com.example.demo.dto.ReservaRequestDTO; // Usando DTOs como record
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    // Criar reserva (Usando ReservaRequestDTO migrado para record)
    @PostMapping
    public ResponseEntity<Reserva> criarReserva(@Valid @RequestBody ReservaRequestDTO body) { // O body deve ser o record
        Reserva criada = reservaService.criarReserva(
                body.clienteId(), // Getters em record são o nome do campo seguido de ()
                body.quartoId(),
                body.dataCheckin(),
                body.dataCheckout(),
                body.tipoPagamento()
        );
        // Retornar a Reserva criada para ser convertida em ReservaResponseDTO no seu Controller
        return ResponseEntity.ok(criada);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reserva> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.buscarPorId(id));
    }

    // Listar reservas de um cliente (Padrão de Filtro por Query Parameter seria a melhoria)
    // Usando o seu endpoint atual:
    @GetMapping("/{clienteId}/cliente")
    public ResponseEntity<List<Reserva>> listarDoCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(reservaService.listarReservasDoCliente(clienteId));
    }

    // URLs Aninhadas e Semânticas para Ações de Estado:
    @PostMapping("/{id}/cancelar")
    public ResponseEntity<Reserva> cancelar(@PathVariable Long id) {
        Reserva salva = reservaService.cancelarReserva(id);
        return ResponseEntity.ok(salva);
    }

    @PostMapping("/{id}/checkin")
    public ResponseEntity<Reserva> checkin(@PathVariable Long id) {
        Reserva salva = reservaService.realizarCheckin(id);
        return ResponseEntity.ok(salva);
    }

    @PostMapping("/{id}/checkout")
    public ResponseEntity<Reserva> checkout(@PathVariable Long id) {
        Reserva salva = reservaService.realizarCheckout(id);
        return ResponseEntity.ok(salva);
    }

    // Nota: O CriarReservaRequest (inner class) foi substituído pelo ReservaRequestDTO (record).
}
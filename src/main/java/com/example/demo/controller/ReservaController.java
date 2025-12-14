package com.example.demo.controller;

import com.example.demo.dto.ReservaRequestDTO;
import com.example.demo.dto.ReservaResponseDTO;
import com.example.demo.model.Reserva;
import com.example.demo.service.ReservaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @PostMapping
    public ResponseEntity<ReservaResponseDTO> criarReserva(@RequestBody @Valid ReservaRequestDTO dto) {

        Reserva reservaCriada = reservaService.criarReserva(
                dto.getClienteId(),
                dto.getQuartoId(),
                dto.getDataCheckin(),
                dto.getDataCheckout(),
                dto.getTipoPagamento()
        );

        // CORREÇÃO: Chama o método estático de conversão (agora existente no DTO)
        return ResponseEntity.status(HttpStatus.CREATED).body(ReservaResponseDTO.fromEntity(reservaCriada));
    }

    // ... (Outros métodos usam fromEntity, e agora funcionarão) ...

    @GetMapping("/{id}")
    public ResponseEntity<ReservaResponseDTO> buscarReservaPorId(@PathVariable Long id) {
        Reserva reserva = reservaService.buscarPorId(id);
        return ResponseEntity.ok(ReservaResponseDTO.fromEntity(reserva));
    }

    // ... e assim por diante.
}
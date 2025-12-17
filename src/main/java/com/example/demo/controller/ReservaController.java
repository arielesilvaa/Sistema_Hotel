package com.example.demo.controller;

import com.example.demo.dto.PagamentoRequestDTO;
import com.example.demo.dto.ReservaRequestDTO;
import com.example.demo.dto.ReservaResponseDTO;
import com.example.demo.model.Reserva;
import com.example.demo.service.ReservaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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

        return ResponseEntity.status(HttpStatus.CREATED).body(ReservaResponseDTO.fromEntity(reservaCriada));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaResponseDTO> buscarReservaPorId(@PathVariable Long id) {
        Reserva reserva = reservaService.buscarPorId(id);
        return ResponseEntity.ok(ReservaResponseDTO.fromEntity(reserva));
    }

    // NOVO ENDPOINT: SIMULAÇÃO DE PAGAMENTO (Recebe o valor no corpo)
    @PatchMapping("/{id}/pagar")
    public ResponseEntity<ReservaResponseDTO> simularPagamento(@PathVariable Long id,
                                                               @RequestBody @Valid PagamentoRequestDTO dto) {
        Reserva reserva = reservaService.simularPagamento(id, dto.getValor());
        return ResponseEntity.ok(ReservaResponseDTO.fromEntity(reserva));
    }

    // CANCELAR RESERVA (MÉTODO HTTP: DELETE)
    @DeleteMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelarReserva(@PathVariable Long id) {
        reservaService.cancelarReserva(id);
        return ResponseEntity.noContent().build();
    }

    // CHECK-IN (MÉTODO HTTP: PATCH)
    @PatchMapping("/{id}/checkin")
    public ResponseEntity<ReservaResponseDTO> realizarCheckin(@PathVariable Long id) {
        Reserva reserva = reservaService.realizarCheckin(id);
        return ResponseEntity.ok(ReservaResponseDTO.fromEntity(reserva));
    }

    // CHECK-OUT (MÉTODO HTTP: PATCH)
    @PatchMapping("/{id}/checkout")
    public ResponseEntity<ReservaResponseDTO> realizarCheckout(@PathVariable Long id) {
        Reserva reserva = reservaService.realizarCheckout(id);
        return ResponseEntity.ok(ReservaResponseDTO.fromEntity(reserva));
    }

    // ENDPOINT DE BUSCA POR CLIENTE
    @GetMapping("/{clienteId}/clientes")
    public ResponseEntity<List<ReservaResponseDTO>> listarReservasPorCliente(@PathVariable Long clienteId) {
        List<ReservaResponseDTO> dtos = reservaService.buscarReservasPorCliente(clienteId)
                .stream()
                .map(ReservaResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}
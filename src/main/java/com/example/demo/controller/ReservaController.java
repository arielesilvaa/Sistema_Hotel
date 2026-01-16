package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.model.Reserva;
import com.example.demo.service.ReservaService;
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.http.HttpStatus;
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

    @PostMapping
    public ResponseEntity<SuccessResponse<EntityModel<ReservaResponseDTO>>> criarReserva(@RequestBody @Valid ReservaRequestDTO dto) {
        Reserva reservaCriada = reservaService.criarReserva(
                dto.clienteId(),
                dto.quartoId(),
                dto.dataCheckin(),
                dto.dataCheckout(),
                dto.tipoPagamento()
        );

        ReservaResponseDTO responseDTO = ReservaResponseDTO.fromEntity(reservaCriada);
        EntityModel<ReservaResponseDTO> resource = EntityModel.of(responseDTO);

        resource.add(linkTo(methodOn(ReservaController.class).buscarReservaPorId(reservaCriada.getId())).withSelfRel());
        resource.add(linkTo(methodOn(ReservaController.class).simularPagamento(reservaCriada.getId(), null)).withRel("pagar"));
        resource.add(linkTo(methodOn(ReservaController.class).cancelarReserva(reservaCriada.getId())).withRel("cancelar"));

        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse<>(resource));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<ReservaResponseDTO>> buscarReservaPorId(@PathVariable Long id) {
        Reserva reserva = reservaService.buscarPorId(id);
        return ResponseEntity.ok(new SuccessResponse<>(ReservaResponseDTO.fromEntity(reserva)));
    }

    @PatchMapping("/{id}/pagamentos")
    public ResponseEntity<SuccessResponse<ReservaResponseDTO>> simularPagamento(@PathVariable Long id, @RequestBody @Valid PagamentoRequestDTO dto) {
        Reserva reserva = reservaService.simularPagamento(id, dto.valor());
        return ResponseEntity.ok(new SuccessResponse<>(ReservaResponseDTO.fromEntity(reserva)));
    }

    @DeleteMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelarReserva(@PathVariable Long id) {
        reservaService.cancelarReserva(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/checkin")
    public ResponseEntity<SuccessResponse<ReservaResponseDTO>> realizarCheckin(@PathVariable Long id) {
        Reserva reserva = reservaService.realizarCheckin(id);
        return ResponseEntity.ok(new SuccessResponse<>(ReservaResponseDTO.fromEntity(reserva)));
    }

    @PatchMapping("/{id}/checkout")
    public ResponseEntity<SuccessResponse<ReservaResponseDTO>> realizarCheckout(@PathVariable Long id) {
        Reserva reserva = reservaService.realizarCheckout(id);
        return ResponseEntity.ok(new SuccessResponse<>(ReservaResponseDTO.fromEntity(reserva)));
    }

    @GetMapping("/{clienteId}/clientes")
    public ResponseEntity<SuccessResponse<List<ReservaResponseDTO>>> listarReservasPorCliente(@PathVariable Long clienteId) {
        List<ReservaResponseDTO> dtos = reservaService.buscarReservasPorCliente(clienteId)
                .stream()
                .map(ReservaResponseDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(new SuccessResponse<>(dtos));
    }
}


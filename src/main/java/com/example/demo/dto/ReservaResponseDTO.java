package com.example.demo.dto;

import com.example.demo.enums.StatusReserva;
import com.example.demo.enums.TipoPagamento;
import com.example.demo.model.Reserva;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDateTime;


public record ReservaResponseDTO (

     Long id,
     Long clienteId,
     Long quartoId,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
     LocalDateTime dataCheckin,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
     LocalDateTime dataCheckout,
     BigDecimal valorTotal,
     TipoPagamento tipoPagamento,
     BigDecimal valorTaxaServico,
     StatusReserva status,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime dataHoraEntrada,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime dataHoraFinalizacao
) {

    // Construtor usado para mapear a Entidade
    public static ReservaResponseDTO fromEntity(Reserva reserva) {
        if (reserva == null) {
            return null;
        }
        return new ReservaResponseDTO(
                reserva.getId(),
                reserva.getCliente() != null ? reserva.getCliente().getId() : null,
                // aqu faz a verificação nula para quarto
                reserva.getQuarto() != null ? reserva.getQuarto().getId() : null,
                // naqui faz a verificação nula para dataCheckin e dataCheckout
                reserva.getDataCheckin(),
                reserva.getDataCheckout(),
                reserva.getValorTotal(),
                reserva.getTipoPagamento(),
                reserva.getValorTaxaServico(),
                reserva.getStatus(),
                reserva.getDataHoraEntrada(),
                reserva.getDataHoraFinalizacao()
        );
    }
}
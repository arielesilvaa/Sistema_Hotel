package com.example.demo.dto;

import com.example.demo.enums.TipoPagamento;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;


public record ReservaRequestDTO(

        @NotNull(message = "clienteId é obrigatório")
        Long clienteId,

        @NotNull(message = "quartoId é obrigatório")
        Long quartoId,

        @NotNull(message = "dataCheckin é obrigatória")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate dataCheckin,

        @NotNull(message = "dataCheckout é obrigatória")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate dataCheckout,

        @NotNull(message = "tipoPagamento é obrigatório")
        TipoPagamento tipoPagamento
) {
}
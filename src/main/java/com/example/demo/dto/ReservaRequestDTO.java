package com.example.demo.dto;

import com.example.demo.enums.TipoPagamento;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;


import java.time.LocalDateTime;

public record ReservaRequestDTO (

    // Getters e Setters
    @NotNull(message = "O ID do cliente é obrigatório.")
    Long clienteId,

    @NotNull(message = "O ID do quarto é obrigatório.")
    Long quartoId,

    @NotNull(message = "A data de check-in é obrigatória.")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime dataCheckin,

    @NotNull(message = "A data de check-out é obrigatória.")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime dataCheckout,

    @NotNull(message = "O tipo de pagamento é obrigatório.")
    TipoPagamento tipoPagamento

) {}
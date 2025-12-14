package com.example.demo.dto;

import com.example.demo.enums.TipoPagamento;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class ReservaRequestDTO {

    // Getters e Setters
    @NotNull(message = "O ID do cliente é obrigatório.")
    private Long clienteId;

    @NotNull(message = "O ID do quarto é obrigatório.")
    private Long quartoId;

    @NotNull(message = "A data de check-in é obrigatória.")
    private LocalDate dataCheckin;

    @NotNull(message = "A data de check-out é obrigatória.")
    private LocalDate dataCheckout;

    @NotNull(message = "O tipo de pagamento é obrigatório.")
    private TipoPagamento tipoPagamento;

}
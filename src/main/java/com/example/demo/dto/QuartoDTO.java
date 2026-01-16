package com.example.demo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;


public record QuartoDTO(

        Long id,

        @NotBlank(message = "Número do quarto é obrigatório")
        String numero,

        @NotNull(message = "Custo diário é obrigatório")
        BigDecimal custoDiario,

        boolean possuiVaranda,
        boolean suite,

        @Min(value = 1, message = "Número de camas deve ser pelo menos 1")
        int numeroCamas
) {

}
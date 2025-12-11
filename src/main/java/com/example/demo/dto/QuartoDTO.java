package com.example.demo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

// A classe é substituída pelo record, e os campos são definidos nos parênteses.
public record QuartoDTO(

        // Identificador único (pode ser opcional na criação, mas é útil para retorno da API)
        Long id,

        // Validações de Constraints (@NotBlank, @NotNull) permanecem nos componentes
        @NotBlank(message = "Número do quarto é obrigatório")
        String numero,

        @NotNull(message = "Custo diário é obrigatório")
        BigDecimal custoDiario,

        // Booleans
        boolean possuiVaranda,
        boolean suite,

        // Int com Validação
        @Min(value = 1, message = "Número de camas deve ser pelo menos 1")
        int numeroCamas
) {
    // O corpo do record é opcional e pode ser deixado vazio.
}
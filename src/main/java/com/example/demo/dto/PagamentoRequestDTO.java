package com.example.demo.dto;


import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record PagamentoRequestDTO (

    // O TipoPagamento já está na Reserva, mas podemos incluí-lo para fins de registro no DTO.
    @NotNull
    BigDecimal valor

) {}
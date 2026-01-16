package com.example.demo.dto;


import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record PagamentoRequestDTO (

    @NotNull
    BigDecimal valor

) {}
package com.example.demo.dto;

import com.example.demo.enums.StatusReserva;
import com.example.demo.enums.TipoPagamento;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

// Record de Resposta final com formatação JSON e campos padronizados
public record ReservaResponseDTO(

        Long id,
        Long clienteId,
        Long quartoId,

        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate dataCheckin,

        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate dataCheckout,

        BigDecimal valorDiaria,
        BigDecimal valorTaxaServico,
        BigDecimal valorTotal,

        TipoPagamento tipoPagamento,
        StatusReserva status,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime dataHoraEntrada,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime dataHoraFinalizacao,

        // Mapeamento dos campos de auditoria: Se a ReservaResponseDTO for construída com campos nomeados assim:
        @JsonProperty("dataCriacao")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt,

        @JsonProperty("dataAtualizacao")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime updatedAt
) {}
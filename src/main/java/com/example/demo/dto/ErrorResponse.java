package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

// Record para padronizar as respostas de erro em JSON
public record ErrorResponse(

        // Status HTTP (ex: 422)
        int status,

        // Mensagem amigável para o cliente (ex: "Quarto já reservado nesse período.")
        String message,

        // Timestamp de quando o erro ocorreu
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime timestamp
) {}

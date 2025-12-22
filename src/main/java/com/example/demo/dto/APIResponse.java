package com.example.demo.dto;

import java.time.LocalDateTime;

public record APIResponse<T> (
        // esse <T> indica que é um tipo generico
    int status, // HTTP status code
    String message, // serve para carregar mensagens de sucesso ou erro
    LocalDateTime timestamp, // esse serve para registrar o momento exato da resposta
    T data //serve para carregar qualquer tipo de dado
){
}

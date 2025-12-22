package com.example.demo.exception;

import lombok.Getter;

// Você pode usar @Getter e @Setter do Lombok, ou definir manualmente.
// Definindo manualmente para garantir que não haja dependências de anotações.
@Getter
public class ErrorResponse {

    private final String message;
    private final int status;

    public ErrorResponse(String message, int status) {
        this.message = message;
        this.status = status;
    }

}
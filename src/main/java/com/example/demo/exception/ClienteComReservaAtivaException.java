package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST) // Retorna 400
public class ClienteComReservaAtivaException extends RuntimeException {

    public ClienteComReservaAtivaException(String message) {
        super(message);
    }
}
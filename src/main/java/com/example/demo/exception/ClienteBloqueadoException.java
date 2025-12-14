package com.example.demo.exception;

public class ClienteBloqueadoException extends RuntimeException {
    public ClienteBloqueadoException(String message) {
        super(message);
    }
}
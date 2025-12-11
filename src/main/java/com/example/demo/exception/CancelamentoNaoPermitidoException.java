package com.example.demo.exception;

public class CancelamentoNaoPermitidoException extends RuntimeException {
    public CancelamentoNaoPermitidoException(String message) {
        super(message);
    }

}

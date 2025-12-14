package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException ex) {
        return new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler({
            ClienteBloqueadoException.class,
            InvalidDataException.class,
            QuartoOcupadoException.class,
            ReservaSobrepostaClienteException.class,
            CancelamentoNaoPermitidoException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleCustomValidationExceptions(RuntimeException ex) {
        return new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        String detailedMessage = "Erro de validação nos campos: " + errors.toString();

        return new ErrorResponse(detailedMessage, HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Trata todas as outras exceções não mapeadas (500 Internal Server Error).
     * Este é o Catch-All que deve capturar o LazyInitializationException ou outros
     * erros inesperados, garantindo uma resposta formatada.
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleAllUncaughtException(Exception ex, WebRequest request) {
        // Logar o erro completo aqui é crucial para debugar:
        // logger.error("Erro interno não tratado", ex);

        // A mensagem de erro retornada deve ser genérica por segurança em produção
        String errorMessage = "Ocorreu um erro interno inesperado no servidor. Consulte os logs para detalhes.";

        // Em ambientes de desenvolvimento/teste, você pode retornar a mensagem de ex.getMessage()
        // para facilitar a depuração.
        if (ex.getMessage() != null && !ex.getMessage().isEmpty()) {
            errorMessage = ex.getMessage(); // Retorna a mensagem da exceção, útil para Lazy Loading Exception
        }


        return new ErrorResponse(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
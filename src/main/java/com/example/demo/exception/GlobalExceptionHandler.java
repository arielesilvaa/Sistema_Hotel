package com.example.demo.exception;

import com.example.demo.dto.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public APIResponse<Void> handleNotFoundException(NotFoundException ex) {
        return new APIResponse<>(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                LocalDateTime.now(),
                null
        );
    }

    @ExceptionHandler({
            ClienteBloqueadoException.class,
            InvalidDataException.class,
            QuartoOcupadoException.class,
            ReservaSobrepostaClienteException.class,
            CancelamentoNaoPermitidoException.class,
            ClienteComReservaAtivaException.class
    })

    // Trata exceções de negócio (400 Bad Request)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public APIResponse<Void> handleCustomValidationExceptions(RuntimeException ex) {
        return new APIResponse<>(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                LocalDateTime.now(),
                null
        );
    }
    // Trata erros de validação de argumentos (400 Bad Request)

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public APIResponse<Void> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        String detailedMessage = "Erro de validação nos campos: " + errors;

        return new APIResponse<>(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                detailedMessage,
                LocalDateTime.now(),
                null
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public APIResponse<Void> handleAllUncaughtException(Exception ex, WebRequest request) {

        String errorMessage = "Ocorreu um erro interno inesperado no servidor. Consulte os logs para detalhes.";

        if (ex.getMessage() != null && !ex.getMessage().isEmpty()) {
            errorMessage = ex.getMessage(); // Retorna a mensagem da exceção, útil para Lazy Loading Exception
        }
        return new APIResponse<>(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                errorMessage,
                LocalDateTime.now(),
                null
        );
    }
}
package com.example.demo.exception;

import com.example.demo.dto.APIResponse; // Importe o seu Record padrão aqui
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestAdvice {

    // 1. Tratamento para Erros de Validação (422 ou 400 conforme sua escolha)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIResponse<Void>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String detailedMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        // data é null, pois é um erro
       APIResponse<Void> body = new APIResponse<>(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Erro de validação nos campos: " + detailedMessage,
                LocalDateTime.now(),
                null
        );
         return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(body);
    }

    // 2. Tratamento para Exceções de Negócio (400)
    @ExceptionHandler({
            InvalidDataException.class,
            QuartoOcupadoException.class,
            ReservaSobrepostaClienteException.class,
            CancelamentoNaoPermitidoException.class,
            ClienteBloqueadoException.class
    })
    public ResponseEntity<APIResponse<Void>> handleBusinessExceptions(Exception ex) {
        APIResponse<Void> body = new APIResponse<>(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                LocalDateTime.now(),
                null
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<APIResponse<Void>> handleNotFoundException(NotFoundException ex) {
       APIResponse<Void> body = new APIResponse<>(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                LocalDateTime.now(),
                null
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<APIResponse<Void>> handleNoHandlerFoundException (NoHandlerFoundException ex) {
        APIResponse<Void> body = new APIResponse<>(
                HttpStatus.NOT_FOUND.value(),
                "Recurso não encontrado: " + ex.getRequestURL(),
                LocalDateTime.now(),
                null
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse<Void>> handleAllUncaughtException(Exception ex) {
        APIResponse<Void> body = new APIResponse<>(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro interno no servidor. Por favor, tente novamente mais tarde.",
                LocalDateTime.now(),
                null
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
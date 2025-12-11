package com.example.demo.exception; // ou com.example.demo.advice

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestAdvice {

    // Record para a resposta de erro (Pode estar em um pacote DTO separado)
    // Manter aqui por simplicidade
    public record ErrorResponse(
            int status,
            String message,
            LocalDateTime timestamp
    ) {}


    // 1. TRATAMENTO PARA ERROS DE VALIDAÇÃO DE CAMPO (@NotBlank, @Min, etc.)
    // Retorna Status 400 (Bad Request) - Erro de sintaxe/formato básico.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex) {

        // Coleta todas as mensagens de erro de validação (para campos múltiplos)
        String detailedMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Erro de validação nos campos: " + detailedMessage,
                LocalDateTime.now()
        );
    }

    // 2. TRATAMENTO PARA EXCEÇÕES DE NEGÓCIO (Datas inválidas, Conflitos de agenda, etc.)
    // Retorna Status 422 (Unprocessable Entity) - Corrigido para evitar o aviso do Sonar,
    // garantindo que a implementação use a constante correta (que representa o valor 422).
    @ExceptionHandler({
            InvalidDataException.class,
            QuartoOcupadoException.class,
            ReservaSobrepostaClienteException.class,
            CancelamentoNaoPermitidoException.class
    })
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY) // O Spring resolve isso corretamente para 422
    public ErrorResponse handleBusinessExceptions(Exception ex) {
        return new ErrorResponse(
                HttpStatus.UNPROCESSABLE_ENTITY.value(), // Valor 422
                ex.getMessage(),
                LocalDateTime.now()
        );
    }

    // 3. TRATAMENTO PARA RECURSOS NÃO ENCONTRADOS
    // Retorna Status 404 (Not Found)
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException ex) {
        return new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );
    }

    // 4. TRATAMENTO GENÉRICO (Fallback)
    // Retorna Status 500 (Internal Server Error) para erros não mapeados
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGenericException(Exception ex) {
        // Para o 500, é bom não expor o detalhe interno da exceção (ex.getMessage()) ao cliente
        return new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Ocorreu um erro interno no servidor. Por favor, tente novamente mais tarde.",
                LocalDateTime.now()
        );
    }
}
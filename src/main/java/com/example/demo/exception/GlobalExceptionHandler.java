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

@RestControllerAdvice // Anotação para tratar exceções globalmente em controladores REST
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

    @ExceptionHandler(MethodArgumentNotValidException.class) // serve para tratar erros de validação de argumentos
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public APIResponse<Void> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        String detailedMessage = "Erro de validação nos campos: " + errors;

        return new APIResponse<>(
                HttpStatus.BAD_REQUEST.value(),
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


    /*
    O GlobalExceptionHandler: A Central de Erros
    Em vez de deixar a API travar e mostrar aquela página de erro feia do Java,
     este código "pega" o erro, limpa ele e "joga" de volta uma resposta bonitinha usando o seu APIResponse.

    Tratando o "Não Encontrado" (NotFoundException):

    Quando o Service não acha um Cliente ou Quarto, ele joga (throw) essa exceção.

    O @ExceptionHandler captura esse erro específico e monta o seu envelope APIResponse com o status 404
     (Not Found). O campo data vai como null, porque não há dado para mostrar, apenas a mensagem de erro.

    Regras de Negócio (O "Pacotão" de Exceções):

    Você agrupou várias exceções como QuartoOcupadoException ou ClienteBloqueadoException.

    O Fluxo: Se o sistema tentar reservar um quarto ocupado, o código "pesca" esse erro e joga um
     400 (Bad Request). Isso avisa ao usuário: "A requisição está certa, mas a regra do hotel não permite isso".

    Validação de Campos (MethodArgumentNotValidException):

    @NotBlank e @Email que usamos nos Records/DTOs? Se o usuário mandar um e-mail inválido,
     o Spring joga esse erro técnico gigante.

    Pega daqui: O código entra nos detalhes do erro (ex.getBindingResult()), percorre todos os
     campos que deram problema e os coloca em um Map (um dicionário de erro).

    Coloca ali: Ele transforma esse dicionário em uma frase legível e coloca na message do seu APIResponse.
     Assim, o usuário sabe exatamente qual campo ele preencheu errado.

    O Erro Fatal (Exception.class):

    Esse é o "pega tudo". Se acontecer um erro que você não previu (tipo o banco de dados cair),
    ele captura, gera um status 500 (Internal Server Error) e envia uma mensagem genérica para não expor
    detalhes sensíveis do seu servidor, mas mantém o padrão do envelope.

    Por que isso é importante tecnicamente?
    Padronização: Não importa se deu erro de digitação ou erro de banco, a resposta do
    servidor terá sempre a mesma cara (o APIResponse)

    Encapsulamento: Você esconde os detalhes técnicos (stacktraces) do Java e entrega apenas o
     que o usuário precisa saber para corrigir o problema.

     Resumo do "Pega e Joga":
    Pega: Qualquer erro (Exception) que acontecer durante o processamento.

    Transforma: Converte o erro técnico em um status HTTP correto (404, 400, 500).

    Joga: Devolve o envelope APIResponse padronizado com a explicação do que houve.
     */
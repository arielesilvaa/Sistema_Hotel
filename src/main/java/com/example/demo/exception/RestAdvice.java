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

@RestControllerAdvice // Anotação para tratar exceções globalmente em controladores REST
public class RestAdvice {

    // 1. Tratamento para Erros de Validação (422 ou 400 conforme sua escolha)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIResponse<Void>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String detailedMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        // data é null, pois é um erro
       APIResponse<Void> body = new APIResponse<>(
                HttpStatus.BAD_REQUEST.value(),
                "Erro de validação nos campos: " + detailedMessage,
                LocalDateTime.now(),
                null
        );
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // 2. T-    )
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

    /*
    O RestAdvice: O Filtro Final da API
    Toda vez que o código "quebra" em algum lugar, o Spring interrompe o fluxo normal e joga o erro para cá.
     O @RestControllerAdvice faz com que este arquivo fique ouvindo todas as falhas da aplicação.

    A "Peneira" de Validação (handleValidationExceptions):

    Quando você tenta cadastrar um cliente com e-mail inválido, o Java gera uma
    MethodArgumentNotValidException.

     O código usa o .stream().map(...) para percorrer todos os erros de campo, junta tudo
     em uma única frase (separada por ponto e vírgula) e gera o "detailedMessage".

     Ele coloca essa lista de erros dentro do seu APIResponse e devolve um 400 (Bad Request).
     Assim, o usuário recebe: "Erro de validação nos campos: email: Email inválido; nome: Nome é obrigatório".

    O Bloco de Regras do Hotel (handleBusinessExceptions):

    Aqui você agrupou os erros específicos do seu negócio (como tentar reservar um quarto já ocupado).

    O código "pesca" a mensagem que você escreveu lá no Service (ex: "Este quarto já está ocupado!") e
    coloca diretamente no campo message do envelope.

    Tratamento de Rotas Inexistentes (handleNoHandlerFoundException):

    Se alguém tentar acessar uma URL que não existe (tipo /api/abacate), o Spring gera esse erro.

    O seu código pega a URL errada que a pessoa digitou (ex.getRequestURL()) e avisa: "Recurso não encontrado".
    É uma forma elegante de dizer que esse endereço não existe.

    A Redução de Danos (handleAllUncaughtException):

    Se acontecer um erro bizarro que ninguém previu (erro 500), o código captura e esconde o erro técnico feio.
    Ele "joga" uma mensagem padrão: "Erro interno no servidor...". Isso é uma medida de segurança, para não
    mostrar detalhes do seu banco de dados ou do seu código para hackers.

🛠️ Diferença Técnica Importante:
Neste arquivo, você está retornando um ResponseEntity<APIResponse<Void>>.

Por que o Void? Porque como é um erro, você não tem "dados" (Clientes ou Reservas) para enviar. O campo
data do seu envelope vai como null.

Por que o ResponseEntity? Para você ter controle total sobre o Status HTTP (400, 404, 500) que vai no
 cabeçalho da resposta, garantindo que o navegador ou o aplicativo entenda o que aconteceu antes mesmo
  de ler o texto.




💡 Resumo do Fluxo:
Entrada: Um erro "bruto" disparado pelo sistema.

Transformação: O código limpa as mensagens técnicas e formata no padrão APIResponse.

Saída: Um JSON padronizado com status, message, timestamp e data: null.

    */
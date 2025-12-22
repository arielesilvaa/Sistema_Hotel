package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
// As anotações do Lombok (Getter, Setter, NoArgsConstructor, AllArgsConstructor) SÃO REMOVIDAS.

// A palavra-chave 'class' é substituída por 'record'.
// Os campos são definidos diretamente na declaração do record.
public record ClienteDTO(
        // Componentes do Record (definem o construtor, getters, equals, hashCode e toString)

        Long id,

        // As anotações de validação permanecem nos componentes
        @NotBlank(message = "Nome é obrigatório") //serve para validar que o campo não está vazio
        @Size(max = 150) //serve para validar o tamanho máximo do campo
        String nome,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido") //valida o formato do email
        String email,

        String telefone,

        String documento
) {
}
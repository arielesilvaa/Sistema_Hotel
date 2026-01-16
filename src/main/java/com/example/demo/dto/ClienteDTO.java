package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record ClienteDTO(
        Long id,

        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 150)
        String nome,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido")
        String email,

        String telefone,

        String documento
) {
}


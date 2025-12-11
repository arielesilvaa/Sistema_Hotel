package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Ignora propriedades específicas do Hibernate durante a serialização JSON
@Entity // Indica que esta classe é uma entidade JPA
@Table(name = "clientes") // Mapeia a entidade para a tabela "clientes"
public class Cliente extends BaseEntity {

    // getters e setters
    @Setter // ele seta o valor do atributo
    @Getter // ele pega o valor do atributo
    @Column(nullable = false) // Define que a coluna não pode ser nula
    private String nome;

    @Setter
    @Getter
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private boolean impedidoDeReservar = false;

    @Getter
    @Setter
    private String telefone;

    @Getter
    @Setter
    private String documento; // CPF ou outro

    public Cliente() {}

    public Cliente(String nome, String email, String telefone, String documento) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.documento = documento;
    }

}

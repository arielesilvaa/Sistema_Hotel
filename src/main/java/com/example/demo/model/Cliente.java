package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "clientes")
public class Cliente extends BaseEntity {

    // getters e setters
    @Setter
    @Getter
    @Column(nullable = false)
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

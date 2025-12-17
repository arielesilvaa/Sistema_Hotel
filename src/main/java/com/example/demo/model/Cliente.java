package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "clientes")
@Getter
@Setter

// CORREÇÃO: Define a ordem desejada dos campos no JSON
@JsonPropertyOrder({"nome", "telefone", "documento", "email", "id", "impedidoDeReservar", "dataAtualizacao", "dataCriacao", "bloqueado"})

public class Cliente extends BaseEntity {

    @Column(nullable = false)
    private String nome;

    @Column(unique = true, nullable = false)
    private String email;

    private String documento;
    private String telefone;
    private boolean bloqueado = false;

    @Column(name = "impedido_de_reservar", nullable = false)
    private Boolean impedidoDeReservar = false;

}
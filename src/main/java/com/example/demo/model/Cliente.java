package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "clientes")
@Getter
@Setter


public class Cliente extends BaseEntity {

    @Column(nullable = false)
    private String nome;

    @Column(unique = true, nullable = false)
    private String email;

    private String documento;
    private String telefone;
    private boolean bloqueado = false;

    @Column(name = "impedido_de_reservar", nullable = false)
    @JsonIgnore
    private Boolean impedidoDeReservar = false;

}
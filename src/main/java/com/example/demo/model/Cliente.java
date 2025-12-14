package com.example.demo.model;

import jakarta.persistence.*;
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
    private Boolean impedidoDeReservar = false;

}
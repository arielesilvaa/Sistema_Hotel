package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity //indica que a classe é uma entidade do JPA
@Table(name = "clientes") //define o nome da tabela no banco de dados
@Getter //pega os dados dos atributos
@Setter //define os dados dos atributos


public class Cliente extends BaseEntity {

    @Column(nullable = false)
    private String nome;

    @Column(unique = true, nullable = false) //garante que o email seja único no banco de dados
    private String email;

    private String documento;
    private String telefone;
    private boolean bloqueado = false;

    @Column(name = "impedido_de_reservar", nullable = false) //define o nome da coluna no banco de dados
    @JsonIgnore // essa anotação serve para ignorar o campo na serialização JSON
    private Boolean impedidoDeReservar = false;

}
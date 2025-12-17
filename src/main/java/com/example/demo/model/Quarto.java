package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "quartos")
@Getter
@Setter
// CORREÇÃO: Define a ordem desejada dos campos no JSON
@JsonPropertyOrder({"custoDiario", "id", "numero", "numeroCamas", "possuiVaranda", "suite", "dataAtualizacao", "dataCriacao"})

public class Quarto extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String numero;

    @Column(name = "custo_diario", nullable = false, precision = 10, scale = 2)
    private BigDecimal custoDiario;

    @Column(name = "numero_camas", nullable = false)
    private Integer numeroCamas; // Alterado para Integer por segurança

    // CORREÇÃO: Tipo alterado para 'Boolean' (Wrapper)
    @Column(name = "possui_varanda", nullable = false)
    private Boolean possuiVaranda = false; // Valor padrão para segurança

    // CORREÇÃO: Tipo alterado para 'Boolean' (Wrapper)
    @Column(nullable = false)
    private Boolean suite = false; // Valor padrão para segurança
}
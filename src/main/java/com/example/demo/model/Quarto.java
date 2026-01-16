package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "quartos")
@Getter
@Setter

public class Quarto extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String numero;

    @Column(name = "custo_diario", nullable = false, precision = 10, scale = 2)
    private BigDecimal custoDiario;

    @Column(name = "numero_camas", nullable = false)
    private Integer numeroCamas;

    @Column(name = "possui_varanda", nullable = false)
    private Boolean possuiVaranda = false;

    @Column(nullable = false)
    private Boolean suite = false;
}
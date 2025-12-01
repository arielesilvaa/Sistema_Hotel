package com.example.demo.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "quartos")
public class Quarto extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String numero;

    @Column(name="custo_diario", precision = 10, scale = 2, nullable=false)
    private BigDecimal custoDiario;

    @Column(nullable = false)
    private boolean possuiVaranda;

    @Column(nullable = false)
    private boolean ehSuite;

    @Column(nullable = false)
    private int numeroCamas;

    public Quarto() {}

    public Quarto(String numero, BigDecimal custoDiario, boolean possuiVaranda, boolean ehSuite, int numeroCamas) {
        this.numero = numero;
        this.custoDiario = custoDiario;
        this.possuiVaranda = possuiVaranda;
        this.ehSuite = ehSuite;
        this.numeroCamas = numeroCamas;
    }

    // getters e setters
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public BigDecimal getCustoDiario() { return custoDiario; }
    public void setCustoDiario(BigDecimal custoDiario) { this.custoDiario = custoDiario; }

    public boolean isPossuiVaranda() { return possuiVaranda; }
    public void setPossuiVaranda(boolean possuiVaranda) { this.possuiVaranda = possuiVaranda; }

    public boolean isEhSuite() { return ehSuite; }
    public void setEhSuite(boolean ehSuite) { this.ehSuite = ehSuite; }

    public int getNumeroCamas() { return numeroCamas; }
    public void setNumeroCamas(int numeroCamas) { this.numeroCamas = numeroCamas; }
}

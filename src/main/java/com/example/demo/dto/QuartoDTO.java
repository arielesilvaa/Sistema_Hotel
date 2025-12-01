package com.example.demo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class QuartoDTO {

    private Long id;

    @NotBlank(message = "Número do quarto é obrigatório")
    private String numero;

    @NotNull(message = "Custo diário é obrigatório")
    private BigDecimal custoDiario;

    private boolean possuiVaranda;
    private boolean ehSuite;

    @Min(value = 1, message = "Número de camas deve ser pelo menos 1")
    private int numeroCamas;

    public QuartoDTO() {}

    public QuartoDTO(Long id, String numero, BigDecimal custoDiario, boolean possuiVaranda, boolean ehSuite, int numeroCamas) {
        this.id = id;
        this.numero = numero;
        this.custoDiario = custoDiario;
        this.possuiVaranda = possuiVaranda;
        this.ehSuite = ehSuite;
        this.numeroCamas = numeroCamas;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

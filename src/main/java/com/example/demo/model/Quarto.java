package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity // Indica que a classe é uma entidade JPA
@Table(name = "quartos") // Mapeia a entidade para a tabela "quartos"
public class Quarto extends BaseEntity {

    // getters e setters
    @Setter
    @Getter
    @Column(nullable = false, unique = true) // Número do quarto não pode ser nulo e deve ser único
    private String numero; // Número do quarto (ex: "101", "202A")

    @Setter
    @Getter
    @Column(name="custo_diario", precision = 10, scale = 2, nullable=false) // Custo diário com precisão e escala definidas
    private BigDecimal custoDiario; // Custo diário do quarto

    @Setter
    @Getter
    @Column(nullable = false) // Indica se o quarto possui varanda
    private boolean possuiVaranda; // Indica se o quarto possui varanda

    @Column(nullable = false) // Indica se o quarto é uma suíte
    private boolean suite; // Indica se o quarto é uma suíte

    @Getter
    @Setter
    @Column(nullable = false) // Número de camas no quarto
    private int numeroCamas; // Número de camas no quarto

    public Quarto() {} // Construtor padrão

    public Quarto(String numero, BigDecimal custoDiario, boolean possuiVaranda, boolean suite, int numeroCamas) {
        this.numero = numero;
        this.custoDiario = custoDiario;
        this.possuiVaranda = possuiVaranda;
        this.suite = suite;
        this.numeroCamas = numeroCamas;
    } // Construtor completo

    public boolean isEhSuite() { return suite; }
    public void setEhSuite(boolean suite) { this.suite = suite; }

}

package com.example.demo.model;

import com.example.demo.enums.StatusReserva;
import com.example.demo.enums.TipoPagamento;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.persistence.FetchType;
import lombok.Getter; // ✅ Adicionado
import lombok.Setter; // ✅ Adicionado
import lombok.NoArgsConstructor; // ✅ Adicionado (para JPA)
import lombok.AllArgsConstructor; // ✅ Adicionado (para o construtor de 8 parâmetros)

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
@Entity
@Table(name = "reservas")
@Getter // ✅ Substitui todos os getters
@Setter // ✅ Substitui todos os setters
@NoArgsConstructor // ✅ Gera o construtor vazio (necessário para JPA)
@AllArgsConstructor // ✅ Gera o construtor com todos os 8 campos (resolve o alerta)
public class Reserva extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cliente_id", nullable = false)
    @JsonIgnore // evita serializar cliente inteiro automaticamente
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "quarto_id", nullable = false)
    @JsonIgnore
    private Quarto quarto;

    @Column(nullable = false)
    private LocalDate dataCheckin;

    @Column(nullable = false)
    private LocalDate dataCheckout;

    @Column(nullable = false)
    private BigDecimal valorDiaria;

    @Column(nullable = false)
    private BigDecimal valorTaxaServico;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoPagamento tipoPagamento;

    @Column(nullable = false)
    private BigDecimal valorTotal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusReserva status = StatusReserva.ABERTA;

    private LocalDateTime dataHoraEntrada;
    private LocalDateTime dataHoraFinalizacao;

    // Construtor vazio e o construtor de 8 parâmetros foram removidos
    // e são gerados pelas anotações @NoArgsConstructor e @AllArgsConstructor

    // Todos os getters e setters (que estavam aqui) foram removidos e
    // são gerados pelas anotações @Getter e @Setter.
}
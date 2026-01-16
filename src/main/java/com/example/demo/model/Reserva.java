package com.example.demo.model;

import com.example.demo.enums.StatusReserva;
import com.example.demo.enums.TipoPagamento;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "quarto_id")
    private Quarto quarto;

    private LocalDateTime dataCheckin;
    private LocalDateTime dataCheckout;

    private LocalDateTime dataHoraPagamento;

    private LocalDateTime dataHoraEntrada;
    private LocalDateTime dataHoraFinalizacao;

    @Enumerated(EnumType.STRING)
    private TipoPagamento tipoPagamento;

    @Enumerated(EnumType.STRING)
    private StatusReserva status;

    private BigDecimal valorDiaria;
    private BigDecimal valorTaxaServico;
    private BigDecimal valorTotal;
}
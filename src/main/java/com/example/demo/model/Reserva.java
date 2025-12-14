package com.example.demo.model;

import com.example.demo.enums.StatusReserva;
import com.example.demo.enums.TipoPagamento;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter // ESSENCIAL: Garante getCliente(), setStatus(), setDataHoraFinalizacao()
@Setter // ESSENCIAL
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

    @Enumerated(EnumType.STRING)
    private StatusReserva status;

    @Enumerated(EnumType.STRING)
    private TipoPagamento tipoPagamento;

    private BigDecimal valorDiaria;
    private BigDecimal valorTotal;
    private BigDecimal valorTaxaServico;

    private LocalDateTime dataHoraEntrada;
    private LocalDateTime dataHoraFinalizacao;
}
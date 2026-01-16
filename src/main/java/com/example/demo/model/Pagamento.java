package com.example.demo.model;

import com.example.demo.enums.TipoPagamento;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "pagamentos")
public class Pagamento extends BaseEntity {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)

    @JoinColumn(name = "reserva_id", nullable = false)
    @JsonIgnore
    private Reserva reserva;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoPagamento tipo;

    @Column(nullable = false)
    private BigDecimal valorPago;

    private LocalDateTime dataPagamento;

    public Pagamento() {
    }

    public Pagamento(Reserva reserva, TipoPagamento tipo, BigDecimal valorPago, LocalDateTime dataPagamento) {
        this.reserva = reserva;
        this.tipo = tipo;
        this.valorPago = valorPago;
        this.dataPagamento = dataPagamento;
    }

}

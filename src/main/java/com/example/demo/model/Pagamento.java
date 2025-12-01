package com.example.demo.model;

import com.example.demo.enums.TipoPagamento;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagamentos")
public class Pagamento extends BaseEntity {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "reserva_id", nullable = false)
    @JsonIgnore // evita N+1 / LazyInitialization na serialização
    private Reserva reserva;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoPagamento tipo;

    @Column(nullable = false)
    private BigDecimal valorPago;

    private LocalDateTime dataPagamento;

    public Pagamento() {}

    public Pagamento(Reserva reserva, TipoPagamento tipo, BigDecimal valorPago, LocalDateTime dataPagamento) {
        this.reserva = reserva;
        this.tipo = tipo;
        this.valorPago = valorPago;
        this.dataPagamento = dataPagamento;
    }

    // getters e setters
    public Reserva getReserva() { return reserva; }
    public void setReserva(Reserva reserva) { this.reserva = reserva; }

    public TipoPagamento getTipo() { return tipo; }
    public void setTipo(TipoPagamento tipo) { this.tipo = tipo; }

    public BigDecimal getValorPago() { return valorPago; }
    public void setValorPago(BigDecimal valorPago) { this.valorPago = valorPago; }

    public LocalDateTime getDataPagamento() { return dataPagamento; }
    public void setDataPagamento(LocalDateTime dataPagamento) { this.dataPagamento = dataPagamento; }
}

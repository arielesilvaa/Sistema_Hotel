package com.example.demo.model;

import com.example.demo.enums.TipoPagamento;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity // Indica que esta classe é uma entidade JPA
@Table(name = "pagamentos") // Mapeia a entidade para a tabela "pagamentos"
public class Pagamento extends BaseEntity {

    @ManyToOne(optional = false, fetch = FetchType.LAZY) // Muitos pagamentos para uma reserva
    //optional = false: pagamento sempre tem que ter uma reserva ligada
    //fetch = FetchType.LAZY: Diz ao JPA para não carregar a reserva inteira automaticamente pagamentos Isso economiza memória e tempo.
    @JoinColumn(name = "reserva_id", nullable = false) // Chave estrangeira para a tabela reservas não pode ser nula
    @JsonIgnore // para não incluir os dados da Reserva quando você pedir um Pagamento erro de recursão.
    private Reserva reserva; // Reserva associada ao pagamento

    @Enumerated(EnumType.STRING) // Armazena o enum como string no banco de dados
    @Column(nullable = false) // Tipo de pagamento não pode ser nulo
    private TipoPagamento tipo; // Tipo de pagamento (ex: CARTAO_CREDITO, DINHEIRO)

    @Column(nullable = false) // Valor pago não pode ser nulo
    private BigDecimal valorPago;

    private LocalDateTime dataPagamento; // Data e hora do pagamento

    public Pagamento() {} // Construtor padrão

    public Pagamento(Reserva reserva, TipoPagamento tipo, BigDecimal valorPago, LocalDateTime dataPagamento) {
        this.reserva = reserva;
        this.tipo = tipo;
        this.valorPago = valorPago;
        this.dataPagamento = dataPagamento;
    } // Construtor completo

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

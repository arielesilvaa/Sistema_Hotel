package com.example.demo.dto;

import com.example.demo.enums.StatusReserva;
import com.example.demo.enums.TipoPagamento;
import com.example.demo.model.Reserva;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservaResponseDTO {

    private Long id;
    private Long clienteId;
    private Long quartoId;
    private LocalDateTime dataCheckin;
    private LocalDateTime dataCheckout;
    private BigDecimal valorTotal;
    private TipoPagamento tipoPagamento;
    private BigDecimal valorTaxaServico;
    private StatusReserva status;
    private LocalDateTime dataHoraEntrada;
    private LocalDateTime dataHoraFinalizacao;

    // Construtor usado para mapear a Entidade
    public ReservaResponseDTO(Reserva reserva) {
        this.id = reserva.getId();
        // Acesso a getCliente() agora funciona graças ao Lombok em Reserva.java
        this.clienteId = reserva.getCliente().getId();
        this.quartoId = reserva.getQuarto().getId();
        this.dataCheckin = reserva.getDataCheckin();
        this.dataCheckout = reserva.getDataCheckout();
        this.valorTotal = reserva.getValorTotal();
        this.tipoPagamento = reserva.getTipoPagamento();
        this.valorTaxaServico = reserva.getValorTaxaServico();
        this.status = reserva.getStatus();
        this.dataHoraEntrada = reserva.getDataHoraEntrada();
        this.dataHoraFinalizacao = reserva.getDataHoraFinalizacao();
    }

    // MÉTODO ESTÁTICO DE CONVERSÃO (RESOLVE O ERRO 'cannot find symbol')
    public static ReservaResponseDTO fromEntity(Reserva reserva) {
        if (reserva == null) {
            return null;
        }
        return new ReservaResponseDTO(reserva);
    }
}
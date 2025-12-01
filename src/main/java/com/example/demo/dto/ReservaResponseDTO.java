package com.example.demo.dto;

import com.example.demo.enums.StatusReserva;
import com.example.demo.enums.TipoPagamento;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ReservaResponseDTO {

    private Long id;
    private Long clienteId;
    private Long quartoId;

    private LocalDate dataCheckin;
    private LocalDate dataCheckout;

    private BigDecimal valorDiaria;
    private BigDecimal valorTaxaServico;
    private BigDecimal valorTotal;

    private TipoPagamento tipoPagamento;
    private StatusReserva status;

    private LocalDateTime dataHoraEntrada;
    private LocalDateTime dataHoraFinalizacao;

    public ReservaResponseDTO() {}

    public ReservaResponseDTO(Long id, Long clienteId, Long quartoId, LocalDate dataCheckin, LocalDate dataCheckout,
                              BigDecimal valorDiaria, BigDecimal valorTaxaServico, BigDecimal valorTotal,
                              TipoPagamento tipoPagamento, StatusReserva status, LocalDateTime dataHoraEntrada,
                              LocalDateTime dataHoraFinalizacao) {
        this.id = id;
        this.clienteId = clienteId;
        this.quartoId = quartoId;
        this.dataCheckin = dataCheckin;
        this.dataCheckout = dataCheckout;
        this.valorDiaria = valorDiaria;
        this.valorTaxaServico = valorTaxaServico;
        this.valorTotal = valorTotal;
        this.tipoPagamento = tipoPagamento;
        this.status = status;
        this.dataHoraEntrada = dataHoraEntrada;
        this.dataHoraFinalizacao = dataHoraFinalizacao;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public Long getQuartoId() { return quartoId; }
    public void setQuartoId(Long quartoId) { this.quartoId = quartoId; }

    public LocalDate getDataCheckin() { return dataCheckin; }
    public void setDataCheckin(LocalDate dataCheckin) { this.dataCheckin = dataCheckin; }

    public LocalDate getDataCheckout() { return dataCheckout; }
    public void setDataCheckout(LocalDate dataCheckout) { this.dataCheckout = dataCheckout; }

    public BigDecimal getValorDiaria() { return valorDiaria; }
    public void setValorDiaria(BigDecimal valorDiaria) { this.valorDiaria = valorDiaria; }

    public BigDecimal getValorTaxaServico() { return valorTaxaServico; }
    public void setValorTaxaServico(BigDecimal valorTaxaServico) { this.valorTaxaServico = valorTaxaServico; }

    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }

    public TipoPagamento getTipoPagamento() { return tipoPagamento; }
    public void setTipoPagamento(TipoPagamento tipoPagamento) { this.tipoPagamento = tipoPagamento; }

    public StatusReserva getStatus() { return status; }
    public void setStatus(StatusReserva status) { this.status = status; }

    public LocalDateTime getDataHoraEntrada() { return dataHoraEntrada; }
    public void setDataHoraEntrada(LocalDateTime dataHoraEntrada) { this.dataHoraEntrada = dataHoraEntrada; }

    public LocalDateTime getDataHoraFinalizacao() { return dataHoraFinalizacao; }
    public void setDataHoraFinalizacao(LocalDateTime dataHoraFinalizacao) { this.dataHoraFinalizacao = dataHoraFinalizacao; }
}

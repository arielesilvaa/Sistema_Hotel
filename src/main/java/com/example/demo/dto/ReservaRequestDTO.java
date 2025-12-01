package com.example.demo.dto;

import com.example.demo.enums.TipoPagamento;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class ReservaRequestDTO {

    @NotNull(message = "clienteId é obrigatório")
    private Long clienteId;

    @NotNull(message = "quartoId é obrigatório")
    private Long quartoId;

    @NotNull(message = "dataCheckin é obrigatória")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dataCheckin;

    @NotNull(message = "dataCheckout é obrigatória")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dataCheckout;

    @NotNull(message = "tipoPagamento é obrigatório")
    private TipoPagamento tipoPagamento;

    public ReservaRequestDTO() {}

    public ReservaRequestDTO(Long clienteId, Long quartoId, LocalDate dataCheckin, LocalDate dataCheckout, TipoPagamento tipoPagamento) {
        this.clienteId = clienteId;
        this.quartoId = quartoId;
        this.dataCheckin = dataCheckin;
        this.dataCheckout = dataCheckout;
        this.tipoPagamento = tipoPagamento;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Long getQuartoId() {
        return quartoId;
    }

    public void setQuartoId(Long quartoId) {
        this.quartoId = quartoId;
    }

    public LocalDate getDataCheckin() {
        return dataCheckin;
    }

    public void setDataCheckin(LocalDate dataCheckin) {
        this.dataCheckin = dataCheckin;
    }

    public LocalDate getDataCheckout() {
        return dataCheckout;
    }

    public void setDataCheckout(LocalDate dataCheckout) {
        this.dataCheckout = dataCheckout;
    }

    public TipoPagamento getTipoPagamento() {
        return tipoPagamento;
    }

    public void setTipoPagamento(TipoPagamento tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }
}

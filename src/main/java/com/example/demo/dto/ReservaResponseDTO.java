package com.example.demo.dto;

import com.example.demo.enums.StatusReserva;
import com.example.demo.enums.TipoPagamento;
import lombok.AllArgsConstructor; // Gera o construtor com todos os 12 parâmetros
import lombok.Getter;           // Gera todos os getters
import lombok.NoArgsConstructor;    // Gera o construtor sem argumentos
import lombok.Setter;           // Gera todos os setters

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    // e o Sonar não irá mais apontar o erro de "muitos parâmetros".
}
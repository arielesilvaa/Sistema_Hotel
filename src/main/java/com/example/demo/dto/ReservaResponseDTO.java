package com.example.demo.dto;

import com.example.demo.enums.StatusReserva;
import com.example.demo.enums.TipoPagamento;
import com.example.demo.model.Reserva;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDateTime;


public record ReservaResponseDTO (

     Long id,
     Long clienteId,
     Long quartoId,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
     LocalDateTime dataCheckin,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
     LocalDateTime dataCheckout,
     BigDecimal valorTotal,
     TipoPagamento tipoPagamento,
     BigDecimal valorTaxaServico,
     StatusReserva status,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime dataHoraEntrada,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime dataHoraFinalizacao
) {

    // Construtor usado para mapear a Entidade
    public static ReservaResponseDTO fromEntity(Reserva reserva) {
        if (reserva == null) {
            return null;
        }
        return new ReservaResponseDTO(
                reserva.getId(),
                reserva.getCliente() != null ? reserva.getCliente().getId() : null,
                // aqu faz a verificação nula para quarto
                reserva.getQuarto() != null ? reserva.getQuarto().getId() : null,
                // naqui faz a verificação nula para dataCheckin e dataCheckout
                reserva.getDataCheckin(),
                reserva.getDataCheckout(),
                reserva.getValorTotal(),
                reserva.getTipoPagamento(),
                reserva.getValorTaxaServico(),
                reserva.getStatus(),
                reserva.getDataHoraEntrada(),
                reserva.getDataHoraFinalizacao()
        );
    }
}

    /*
      (@JsonFormat): O código pega as datas brutas do Java e as "formata" em um padrão universal

     fromEntity: Ele funciona como uma ponte de tradução.

     Ele recebe uma Reserva completa que veio do banco de dados.

     Veja que ele faz várias checagens como reserva.getCliente() != null ? ....
     Isso é para o código não "quebrar" se uma reserva estiver sem cliente ou sem quarto. ,
     Ele tenta pegar o ID e, se não encontrar, coloca um null no lugar de forma segura.
     Ele "extrai" apenas os IDs e os valores e joga para dentro do construtor do ReservaResponseDTO.


     Resumo da "Conexão" Técnica:
    Entrada: Recebe a Entidade Reserva (cheia de conexões complexas com o banco).

    Ação: Filtra os dados, formata as datas e resolve as pendências de valores nulos (Null-safe).

    Saída: Entrega um JSON enxuto, fácil de ler e pronto para ser exibido na tela do usuário.


     */
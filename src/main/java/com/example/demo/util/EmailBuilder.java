package com.example.demo.util;

import com.example.demo.model.Reserva;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public final class EmailBuilder {

    private static final Locale BRAZIL_LOCALE = Locale.forLanguageTag("pt-BR"); // Define a configuração de idioma e país como pt-BR.
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Formato de data padrão.

    private EmailBuilder() {} // Construtor privado para evitar instanciação.

    public static String buildReservaEmailBody(Reserva reserva) {
        if (reserva == null) return ""; // Retorna string vazia se a reserva for nula.

        // Usamos a constante estática.
        NumberFormat nf = NumberFormat.getCurrencyInstance(BRAZIL_LOCALE); // Formato de moeda para Brasil.
        nf.setRoundingMode(RoundingMode.HALF_UP); // Define o modo de arredondamento para evitar exceções.

        StringBuilder sb = new StringBuilder(); // Usado para construir o corpo do e-mail de forma eficiente.
        sb.append("Olá ").append(reserva.getCliente() != null ? reserva.getCliente().getNome() : "Hóspede").append(",\n\n"); //é uma cheveagem de segurança: se o cliente existir, pega o nome; se não, usa o termo genérico "Hóspede"
        sb.append("Sua reserva foi criada com sucesso. Seguem os detalhes:\n\n"); // Introdução

        sb.append("Check-in: ").append(reserva.getDataCheckin() != null ? reserva.getDataCheckin().format(DATE_FORMATTER) : "N/A").append("\n");
        sb.append("Check-out: ").append(reserva.getDataCheckout() != null ? reserva.getDataCheckout().format(DATE_FORMATTER) : "N/A").append("\n");

        long dias = (reserva.getDataCheckin() != null && reserva.getDataCheckout() != null)
                ? ChronoUnit.DAYS.between(reserva.getDataCheckin(), reserva.getDataCheckout())
                : 0;
        sb.append("Diárias: ").append(dias).append("\n\n"); //Calcula o número de dias entre o check-in e o check-out (se ambas as datas existirem) usando o ChronoUnit

        sb.append("Valor da diária: ").append(reserva.getValorDiaria() != null ? nf.format(reserva.getValorDiaria()) : "N/A").append("\n"); //Valor da diária com a formatação correta R$
        sb.append("Valor taxa de serviço: ").append(reserva.getValorTaxaServico() != null ? nf.format(reserva.getValorTaxaServico()) : "N/A").append("\n"); //Valor da taxa de serviço com a formatação correta R$
        sb.append("Valor total: ").append(reserva.getValorTotal() != null ? nf.format(reserva.getValorTotal()) : "N/A").append("\n\n"); //Valor total com a formatação correta R$

        sb.append("Status da reserva: ").append(reserva.getStatus() != null ? reserva.getStatus().name() : "N/A").append("\n"); // Status da reserva
        sb.append("ID da reserva: ").append(reserva.getId() != null ? reserva.getId() : "N/A").append("\n\n"); // ID da reserva

        sb.append("Se tiver alguma dúvida, responda este e-mail.\n\n"); // Instrução para dúvidas
        sb.append("Atenciosamente,\nEquipe do Hotel\n"); // Fechamento do e-mail

        return sb.toString(); // Retorna o corpo do e-mail como string.
    }
}
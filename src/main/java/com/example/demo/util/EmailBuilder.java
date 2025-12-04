package com.example.demo.util;

import com.example.demo.model.Reserva;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public final class EmailBuilder {

    // ✅ CORRIGIDO: Usando forLanguageTag, que é a forma moderna e evita o alerta do Sonar.
    private static final Locale BRAZIL_LOCALE = Locale.forLanguageTag("pt-BR");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private EmailBuilder() {}

    /**
     * Constrói o corpo do e-mail para a reserva recebida.
     * Usa Locale pt-BR para formatar valores monetários.
     */
    public static String buildReservaEmailBody(Reserva reserva) {
        if (reserva == null) return "";

        // Usamos a constante estática.
        NumberFormat nf = NumberFormat.getCurrencyInstance(BRAZIL_LOCALE);
        nf.setRoundingMode(RoundingMode.HALF_UP);

        StringBuilder sb = new StringBuilder();
        sb.append("Olá ").append(reserva.getCliente() != null ? reserva.getCliente().getNome() : "Hóspede").append(",\n\n");
        sb.append("Sua reserva foi criada com sucesso. Seguem os detalhes:\n\n");

        sb.append("Check-in: ").append(reserva.getDataCheckin() != null ? reserva.getDataCheckin().format(DATE_FORMATTER) : "N/A").append("\n");
        sb.append("Check-out: ").append(reserva.getDataCheckout() != null ? reserva.getDataCheckout().format(DATE_FORMATTER) : "N/A").append("\n");

        long dias = (reserva.getDataCheckin() != null && reserva.getDataCheckout() != null)
                ? ChronoUnit.DAYS.between(reserva.getDataCheckin(), reserva.getDataCheckout())
                : 0;
        sb.append("Diárias: ").append(dias).append("\n\n");

        sb.append("Valor da diária: ").append(reserva.getValorDiaria() != null ? nf.format(reserva.getValorDiaria()) : "N/A").append("\n");
        sb.append("Valor taxa de serviço: ").append(reserva.getValorTaxaServico() != null ? nf.format(reserva.getValorTaxaServico()) : "N/A").append("\n");
        sb.append("Valor total: ").append(reserva.getValorTotal() != null ? nf.format(reserva.getValorTotal()) : "N/A").append("\n\n");

        sb.append("Status da reserva: ").append(reserva.getStatus() != null ? reserva.getStatus().name() : "N/A").append("\n");
        sb.append("ID da reserva: ").append(reserva.getId() != null ? reserva.getId() : "N/A").append("\n\n");

        sb.append("Se tiver alguma dúvida, responda este e-mail.\n\n");
        sb.append("Atenciosamente,\nEquipe do Hotel\n");

        return sb.toString();
    }
}
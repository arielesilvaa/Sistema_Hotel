package com.example.demo.service;

import com.example.demo.model.Reserva;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
public class SimpleEmailService implements EmailService {

    // Simula envio de email — em produção substitua por JavaMailSender
    @Override
    public void enviarEmailReserva(Reserva reserva) {
        StringBuilder sb = new StringBuilder();
        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        nf.setRoundingMode(RoundingMode.HALF_UP);

        sb.append("Olá ").append(reserva.getCliente().getNome()).append(",\n\n");
        sb.append("Sua reserva foi criada com sucesso. Dados:\n");
        sb.append("Check-in: ").append(reserva.getDataCheckin().format(dateFmt)).append("\n");
        sb.append("Check-out: ").append(reserva.getDataCheckout().format(dateFmt)).append("\n");

        long dias = java.time.temporal.ChronoUnit.DAYS.between(reserva.getDataCheckin(), reserva.getDataCheckout());
        sb.append("Diárias: ").append(dias).append("\n");
        sb.append("Valor da diária: ").append(nf.format(reserva.getValorDiaria())).append("\n");
        sb.append("Valor taxa de serviço: ").append(nf.format(reserva.getValorTaxaServico())).append("\n");
        sb.append("Valor total: ").append(nf.format(reserva.getValorTotal())).append("\n\n");

        sb.append("ID da reserva: ").append(reserva.getId()).append("\n");
        sb.append("\nAtenciosamente,\nEquipe do Hotel (simulado)\n");

        // Aqui apenas logamos — substitua por envio real (JavaMailSender) se quiser
        System.out.println("=== Simulação de envio de e-mail ===");
        System.out.println(sb.toString());
        System.out.println("=== Fim do e-mail ===");
    }
}

package com.example.demo.service;

import com.example.demo.model.Reserva;
import com.example.demo.enums.TipoPagamento;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
public class SimpleEmailService implements EmailService {

    // Configurações (devem ser as mesmas usadas no ReservaService)
    private static final BigDecimal ACRESCIMO_CREDITO = new BigDecimal("0.05"); // 5%

    // Simula envio de email — em produção substitua por JavaMailSender
    @Override
    public void enviarEmailReserva(Reserva reserva) {
        StringBuilder sb = new StringBuilder();
        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // Formato de moeda brasileira
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        nf.setRoundingMode(RoundingMode.HALF_UP);

        sb.append("Olá ").append(reserva.getCliente().getNome()).append(",\n\n");
        sb.append("Sua reserva foi criada com sucesso. Dados:\n");
        sb.append("Check-in: ").append(reserva.getDataCheckin().format(dateFmt)).append("\n");
        sb.append("Check-out: ").append(reserva.getDataCheckout().format(dateFmt)).append("\n");

        long dias = java.time.temporal.ChronoUnit.DAYS.between(reserva.getDataCheckin(), reserva.getDataCheckout());
        sb.append("Diárias: ").append(dias).append("\n");

        // --- Detalhes dos Valores ---

        sb.append("Valor da diária: ").append(nf.format(reserva.getValorDiaria())).append("\n");
        sb.append("Valor taxa de serviço: ").append(nf.format(reserva.getValorTaxaServico())).append("\n");

        // 🎯 Lógica para mostrar o acréscimo de crédito
        if (reserva.getTipoPagamento() == TipoPagamento.CREDITO) {

            // Re-calcula o valor base antes do acréscimo de crédito (ValorTotal / 1.05)
            // Se o valor total = Subtotal + Taxa + Acréscimo (5% do (Subtotal + Taxa))
            // O valor total antes do acréscimo é: (ValorTotal / 1.05)

            BigDecimal fatorAcrecimo = BigDecimal.ONE.add(ACRESCIMO_CREDITO); // 1.05
            BigDecimal valorBase = reserva.getValorTotal().divide(fatorAcrecimo, 2, RoundingMode.HALF_UP);

            // O valor do acréscimo é a diferença entre o total e o valor base
            BigDecimal valorAcrescimo = reserva.getValorTotal().subtract(valorBase);

            // Adiciona a linha ao e-mail
            sb.append("Acréscimo de 5% (Pagamento c/ Crédito): ").append(nf.format(valorAcrescimo)).append("\n");
        }

        sb.append("----------------------------\n");
        sb.append("Valor TOTAL: ").append(nf.format(reserva.getValorTotal())).append("\n");
        sb.append("Tipo de pagamento: ").append(reserva.getTipoPagamento()).append("\n\n");

        sb.append("ID da reserva: ").append(reserva.getId()).append("\n");
        sb.append("\nAtenciosamente,\nEquipe do Hotel (simulado)\n");

        System.out.println("=== Simulação de envio de e-mail ===");
        System.out.println(sb.toString());
        System.out.println("=== Fim do e-mail ===");
    }
}
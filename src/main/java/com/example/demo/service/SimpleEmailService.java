package com.example.demo.service;

import com.example.demo.model.Reserva;
import com.example.demo.enums.TipoPagamento;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage; // Importante
import org.springframework.mail.javamail.JavaMailSender; // Importante
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
public class SimpleEmailService implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(SimpleEmailService.class);
    private static final Locale BRAZIL_LOCALE = Locale.forLanguageTag("pt-BR");

    // ✅ NOVO: Injeção do JavaMailSender
    private final JavaMailSender emailSender;

    // Configuráveis
    private static final BigDecimal ACRESCIMO_CREDITO = new BigDecimal("0.05"); // 5%

    // ✅ NOVO: Construtor para injeção de dependência (JavaMailSender)
    public SimpleEmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public void enviarEmailReserva(Reserva reserva) {

        // 1. Construir o corpo do e-mail como antes (StringBuilder)
        StringBuilder sb = new StringBuilder();
        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        NumberFormat nf = NumberFormat.getCurrencyInstance(BRAZIL_LOCALE);
        nf.setRoundingMode(RoundingMode.HALF_UP);

        sb.append("Olá ").append(reserva.getCliente().getNome()).append(",\n\n");
        sb.append("Sua reserva foi criada com sucesso. Dados:\n");
        sb.append("Check-in: ").append(reserva.getDataCheckin().format(dateFmt)).append("\n");
        sb.append("Check-out: ").append(reserva.getDataCheckout().format(dateFmt)).append("\n");

        long dias = java.time.temporal.ChronoUnit.DAYS.between(reserva.getDataCheckin(), reserva.getDataCheckout());
        sb.append("Diárias: ").append(dias).append("\n");

        sb.append("Valor da diária: ").append(nf.format(reserva.getValorDiaria())).append("\n");
        sb.append("Valor taxa de serviço: ").append(nf.format(reserva.getValorTaxaServico())).append("\n");

        if (reserva.getTipoPagamento() == TipoPagamento.CREDITO) {
            BigDecimal fatorAcrecimo = BigDecimal.ONE.add(ACRESCIMO_CREDITO); // 1.05
            BigDecimal valorBase = reserva.getValorTotal().divide(fatorAcrecimo, 2, RoundingMode.HALF_UP);
            BigDecimal valorAcrescimo = reserva.getValorTotal().subtract(valorBase);
            sb.append("Acréscimo de 5% (Pagamento c/ Crédito): ").append(nf.format(valorAcrescimo)).append("\n");
        }

        sb.append("----------------------------\n");
        sb.append("Valor TOTAL: ").append(nf.format(reserva.getValorTotal())).append("\n");
        sb.append("Tipo de pagamento: ").append(reserva.getTipoPagamento()).append("\n\n");

        sb.append("ID da reserva: ").append(reserva.getId()).append("\n");
        sb.append("\nAtenciosamente,\nEquipe do Hotel\n");

        // 2. Criar e enviar o e-mail usando JavaMailSender
        SimpleMailMessage message = new SimpleMailMessage();

        // ENDEREÇO DE REMETENTE E DESTINATÁRIO
        message.setFrom("hotel-simulado@seuhotel.com");
        message.setTo(reserva.getCliente().getEmail());

        message.setSubject("Confirmação de Reserva #" + reserva.getId());
        message.setText(sb.toString()); // Usa o corpo que você construiu

        try {
            emailSender.send(message);
            logger.info("E-mail de confirmação enviado com sucesso para: {}", reserva.getCliente().getEmail());
        } catch (Exception e) {
            // Se falhar, registra no logger, mas não interrompe a aplicação (conforme sua lógica original)
            logger.error("Falha ao enviar e-mail de confirmação da reserva {}: {}", reserva.getId(), e.getMessage());
        }
    }
}
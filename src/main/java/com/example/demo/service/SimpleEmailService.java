package com.example.demo.service;

import com.example.demo.model.Reserva;
import com.example.demo.enums.TipoPagamento;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service // Anotação para registrar como um bean de serviço
public class SimpleEmailService implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(SimpleEmailService.class); // Logger
    private static final Locale BRAZIL_LOCALE = Locale.forLanguageTag("pt-BR"); // Locale para Brasil

    // Injeção de dependência do JavaMailSender
    private final JavaMailSender emailSender;

    // Constante para acréscimo de 5% no pagamento com cartão de crédito
    private static final BigDecimal ACRESCIMO_CREDITO = new BigDecimal("0.05"); // 5%

    // Construtor
    public SimpleEmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    } // Injeção de dependência via construtor

    @Override // Implementação obrigatória da interface. Recebe a reserva com todos os dados.
    public void enviarEmailReserva(Reserva reserva) {

        StringBuilder sb = new StringBuilder(); // Usado para construir o corpo do e-mail de forma eficiente como data e hora
        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Formato de data
        NumberFormat nf = NumberFormat.getCurrencyInstance(BRAZIL_LOCALE); // Formato de moeda para Brasil
        nf.setRoundingMode(RoundingMode.HALF_UP); // Define o modo de arredondamento pra noa gerar uma exceção

        sb.append("Olá ").append(reserva.getCliente().getNome()).append(",\n\n"); // Saudação personalizada
        sb.append("Sua reserva foi criada com sucesso. Dados:\n"); // Introdução
        sb.append("Check-in: ").append(reserva.getDataCheckin().format(dateFmt)).append("\n");
        sb.append("Check-out: ").append(reserva.getDataCheckout().format(dateFmt)).append("\n");
        //appen -> ele adiciona texto ao StringBuilder
        // StringBuilder -> ele monta string de forma mais eficiente pricipalmente quando tem muitas conteúdos de texto

        long dias = java.time.temporal.ChronoUnit.DAYS.between(reserva.getDataCheckin(), reserva.getDataCheckout());
        // Calcula o número de dias entre check-in e check-out
        sb.append("Diárias: ").append(dias).append("\n");

        sb.append("Valor da diária: ").append(nf.format(reserva.getValorDiaria())).append("\n");
        sb.append("Valor taxa de serviço: ").append(nf.format(reserva.getValorTaxaServico())).append("\n");

        if (reserva.getTipoPagamento() == TipoPagamento.CREDITO) {
            //Esta parte re-calcula o valor do acréscimo de crédito (os 5%) para mostrar o detalhe no corpo do e-mail
            BigDecimal fatorAcrecimo = BigDecimal.ONE.add(ACRESCIMO_CREDITO); // 1.05
            BigDecimal valorBase = reserva.getValorTotal().divide(fatorAcrecimo, 2, RoundingMode.HALF_UP); // Calcula o valor base sem acréscimo
            BigDecimal valorAcrescimo = reserva.getValorTotal().subtract(valorBase); // Calcula o valor do acréscimo
            sb.append("Acréscimo de 5% (Pagamento c/ Crédito): ").append(nf.format(valorAcrescimo)).append("\n");
        }

        sb.append("----------------------------\n");
        sb.append("Valor TOTAL: ").append(nf.format(reserva.getValorTotal())).append("\n");
        sb.append("Tipo de pagamento: ").append(reserva.getTipoPagamento()).append("\n\n");

        sb.append("ID da reserva: ").append(reserva.getId()).append("\n");
        sb.append("\nAtenciosamente,\nEquipe do Hotel\n");

        // 2. Criar e enviar o e-mail usando JavaMailSender
        SimpleMailMessage message = new SimpleMailMessage(); // Cria uma nova mensagem simples

        // ENDEREÇO DE REMETENTE E DESTINATÁRIO
        message.setFrom("hotel-simulado@seuhotel.com"); // E-mail do hotel (remetente)
        message.setTo(reserva.getCliente().getEmail()); // E-mail do cliente da reserva

        message.setSubject("Confirmação de Reserva #" + reserva.getId()); // Assunto do e-mail
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
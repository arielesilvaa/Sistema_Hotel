package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * Configuração opcional do JavaMailSender.
 *
 * Para ativar, preencha as propriedades no application.properties:
 * spring.mail.host=smtp.mailtrap.io
 * spring.mail.port=2525
 * spring.mail.username=seu_user
 * spring.mail.password=sua_senha
 * spring.mail.protocol=smtp
 *
 * Se você preferir não usar envio real e manter o SimpleEmailService simulado,
 * pode deixar essas propriedades vazias.
 */
@Configuration
public class EmailConfig {

    @Value("${spring.mail.host:}")
    private String host;

    @Value("${spring.mail.port:0}")
    private Integer port;

    @Value("${spring.mail.username:}")
    private String username;

    @Value("${spring.mail.password:}")
    private String password;

    @Value("${spring.mail.protocol:smtp}")
    private String protocol;

    @Value("${spring.mail.properties.mail.smtp.auth:true}")
    private String mailSmtpAuth;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable:true}")
    private String mailStartTls;

    @Bean
    public JavaMailSender javaMailSender() {
        // Se não configurado, ainda retornamos um JavaMailSenderImpl com valores básicos.
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        if (host == null || host.isBlank() || port == null || port == 0) {
            // Não configurado — retorna um sender padrão (não funcionará até preencher props)
            return mailSender;
        }

        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);
        mailSender.setProtocol(protocol);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", protocol);
        props.put("mail.smtp.auth", mailSmtpAuth);
        props.put("mail.smtp.starttls.enable", mailStartTls);
        props.put("mail.debug", "false");

        return mailSender;
    }
}

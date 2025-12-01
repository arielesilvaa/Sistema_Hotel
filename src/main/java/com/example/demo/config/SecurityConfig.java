package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    /**
     * Configuração simples para desenvolvimento:
     * - permite acesso ao H2 Console
     * - desabilita frameOptions para que o console carregue em iframe
     * - desabilita CSRF apenas para o h2-console
     * - permite todas as requisições (sem autenticação)
     *
     * ATENÇÃO: em produção você NÃO deve permitir tudo — deve adicionar autenticação.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Permite o H2 console (desativa CSRF apenas para este path)
                .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
                // Permite frames (H2 console usa iframe)
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                // Permite todas as requisições (desliga proteção para facilitar dev)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                );

        return http.build();
    }
}

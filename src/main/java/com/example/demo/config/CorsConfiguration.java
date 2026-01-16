package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// esse arquivo é para configurar o CORS globalmente na aplicação
@Configuration // Anotação para indicar que esta classe é uma configuração do Spring
public class CorsConfiguration implements WebMvcConfigurer {

    @Override // Configurações de CORS
    public void addCorsMappings(CorsRegistry registry) {
        // ESSA LINHA É CRUCIAL: Mapeia para TODAS AS ROTAS (incluindo /api/reservas)
        registry.addMapping("/**") //serve para todas as rotas
                // Permite requisições da porta padrão do React
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Permite todos os métodos HTTP
                .allowedHeaders("*") // Permite todos os cabeçalhos
                .allowCredentials(true); // Permite envio de cookies e credenciais
    }
}
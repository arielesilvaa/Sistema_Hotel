package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

// Indica que esta classe é uma superclasse mapeada,
// e suas propriedades devem ser incluídas nas classes filhas (Entidades).
@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {

    // Chave primária padrão para todas as entidades
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Campos de auditoria: data de criação
    @JsonProperty("dataCriacao") // Garante que no JSON o nome seja "dataCriacao"
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    // Campos de auditoria: data de última atualização
    @JsonProperty("dataAtualizacao") // Garante que no JSON o nome seja "dataAtualizacao"
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    // Método executado automaticamente ANTES de salvar (primeira vez)
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Método executado automaticamente ANTES de atualizar
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
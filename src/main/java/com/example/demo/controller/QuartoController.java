package com.example.demo.controller;

import com.example.demo.model.Quarto;
import com.example.demo.service.QuartoService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController // Indica que essa classe é um controlador REST
@RequestMapping("/api/quartos")// Mapeia as requisições que começam com /api/quartos para esse controlador
public class QuartoController {

    private final QuartoService quartoService; // Dependência do serviço de quarto

    public QuartoController(QuartoService quartoService) {
        this.quartoService = quartoService;
    } // Injeção de dependência via construtor

    @PostMapping // Mapeia requisições POST para esse método
    public ResponseEntity<Quarto> criarQuarto(@RequestBody Quarto request) {
        Quarto criado = quartoService.criar(request);
        return ResponseEntity.ok(criado);
    } // O @RequestBody indica que o corpo da requisição será convertido em um objeto Quarto

    @GetMapping // Mapeia requisições GET para esse método
    public ResponseEntity<List<Quarto>> listar() {
        return ResponseEntity.ok(quartoService.listarTodos());
    } // Retorna uma lista de todos os quartos

    @GetMapping("/{id}") // Mapeia requisições GET com um ID específico
    public ResponseEntity<Quarto> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(quartoService.buscarPorId(id));
    } // O @PathVariable indica que o ID virá da URL

    @PutMapping("/{id}") // Mapeia requisições PUT com um ID específico
    public ResponseEntity<Quarto> atualizar(@PathVariable Long id, @RequestBody Quarto request) {
        Quarto atualizado = quartoService.atualizar(id, request);
        return ResponseEntity.ok(atualizado);
    } // Atualiza os dados do quarto com o ID fornecido

    // Endpoint específico para atualizar apenas o valor
    @PatchMapping("/{id}/valor") // Mapeia requisições PATCH para esse método
    public ResponseEntity<Quarto> atualizarValor(@PathVariable Long id, @RequestBody ValorRequest body) {
        Quarto atualizado = quartoService.atualizarValor(id, body.getNovoValor());
        return ResponseEntity.ok(atualizado);
    } // Atualiza apenas o valor do quarto com o ID fornecido

    @DeleteMapping("/{id}") // Mapeia requisições DELETE com um ID específico
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        quartoService.deletar(id);
        return ResponseEntity.noContent().build();
    } // Deleta o quarto com o ID fornecido

    // DTO é uma classe simples usada para transferir dados
    @Setter
    @Getter
    public static class ValorRequest {
        private BigDecimal novoValor;

    }
}

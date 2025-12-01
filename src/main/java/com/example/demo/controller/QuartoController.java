package com.example.demo.controller;

import com.example.demo.model.Quarto;
import com.example.demo.service.QuartoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/quartos")
public class QuartoController {

    private final QuartoService quartoService;

    public QuartoController(QuartoService quartoService) {
        this.quartoService = quartoService;
    }

    @PostMapping
    public ResponseEntity<Quarto> criarQuarto(@RequestBody Quarto request) {
        Quarto criado = quartoService.criar(request);
        return ResponseEntity.ok(criado);
    }

    @GetMapping
    public ResponseEntity<List<Quarto>> listar() {
        return ResponseEntity.ok(quartoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Quarto> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(quartoService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Quarto> atualizar(@PathVariable Long id, @RequestBody Quarto request) {
        Quarto atualizado = quartoService.atualizar(id, request);
        return ResponseEntity.ok(atualizado);
    }

    // Endpoint específico para atualizar apenas o valor
    @PatchMapping("/{id}/valor")
    public ResponseEntity<Quarto> atualizarValor(@PathVariable Long id, @RequestBody ValorRequest body) {
        Quarto atualizado = quartoService.atualizarValor(id, body.getNovoValor());
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        quartoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // DTO
    public static class ValorRequest {
        private BigDecimal novoValor;

        public BigDecimal getNovoValor() { return novoValor; }
        public void setNovoValor(BigDecimal novoValor) { this.novoValor = novoValor; }
    }
}

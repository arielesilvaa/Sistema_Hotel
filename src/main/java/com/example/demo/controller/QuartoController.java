package com.example.demo.controller;

import com.example.demo.dto.APIResponse;
import com.example.demo.dto.SuccessResponse;
import com.example.demo.model.Quarto;
import com.example.demo.service.QuartoService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/quartos")
public class QuartoController {

    private final QuartoService quartoService;

    public QuartoController(QuartoService quartoService) {
        this.quartoService = quartoService;
    }

    @PostMapping
    public ResponseEntity<SuccessResponse<EntityModel<Quarto>>> criarQuarto(@RequestBody Quarto request) {
        Quarto criado = quartoService.criar(request);
        EntityModel<Quarto> resource = EntityModel.of(criado);

        resource.add(linkTo(methodOn(QuartoController.class).buscar(criado.getId())).withSelfRel());
        resource.add(linkTo(methodOn(QuartoController.class).atualizarValor(criado.getId(), null)).withRel("alterar_preco"));

        return ResponseEntity.status(201).body(new SuccessResponse<>(resource));
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<List<Quarto>>> listar() {
        return ResponseEntity.ok(new SuccessResponse<>(quartoService.listarTodos()));
    }

    @GetMapping("/{id}/quartos")
    public ResponseEntity<SuccessResponse<Quarto>> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(new SuccessResponse<>(quartoService.buscarPorId(id)));
    }

    @PatchMapping("/{id}/valores")
    public ResponseEntity<SuccessResponse<Quarto>> atualizarValor(@PathVariable Long id, @RequestBody ValorRequest body) {
        return ResponseEntity.ok(new SuccessResponse<>(quartoService.atualizarValor(id, body.getNovoValor())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        quartoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @Setter @Getter
    public static class ValorRequest { private BigDecimal novoValor; }
}


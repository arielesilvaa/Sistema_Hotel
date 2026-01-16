package com.example.demo.controller;

import com.example.demo.dto.SuccessResponse;
import com.example.demo.model.Cliente;
import com.example.demo.service.ClienteService;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<SuccessResponse<EntityModel<Cliente>>> criar(@RequestBody Cliente request) {
        Cliente criado = clienteService.criar(request);
        EntityModel<Cliente> resource = EntityModel.of(criado);

        resource.add(linkTo(methodOn(ClienteController.class).buscar(criado.getId())).withSelfRel());
        resource.add(linkTo(methodOn(ClienteController.class).deletar(criado.getId())).withRel("deletar"));
        resource.add(linkTo(methodOn(ClienteController.class).listar()).withRel("listar_todos"));

        return ResponseEntity.status(201).body(new SuccessResponse<>(resource));
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<List<Cliente>>> listar() {
        return ResponseEntity.ok(new SuccessResponse<>(clienteService.listarTodos()));
    }

    @GetMapping("/{id}/clientes")
    public ResponseEntity<SuccessResponse<Cliente>> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(new SuccessResponse<>(clienteService.buscarPorId(id)));
    }

    @PutMapping("/{id}/clientes")
    public ResponseEntity<SuccessResponse<Cliente>> atualizar(@PathVariable Long id, @RequestBody Cliente request) {
        return ResponseEntity.ok(new SuccessResponse<>(clienteService.atualizar(id, request)));
    }

    @DeleteMapping("/{id}/clientes")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        clienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}



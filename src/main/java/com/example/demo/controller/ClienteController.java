package com.example.demo.controller;

import com.example.demo.model.Cliente;
import com.example.demo.service.ClienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController //fala que classe é um controlador REST, cuida das requisições HTTP
@RequestMapping("/api/clientes") //mapeia as requisições que começam com /api/clientes para esse controlador
public class ClienteController {

    private final ClienteService clienteService; //dependência do serviço de cliente

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    } //injeção de dependência via construtor

    @PostMapping //mapeia requisições POST para esse método
    public ResponseEntity<Cliente> criar(@RequestBody Cliente request) {
        Cliente criado = clienteService.criar(request);
        return ResponseEntity.ok(criado);
    } //o @RequestBody indica que o corpo da requisição será convertido em um objeto Cliente

    @GetMapping //mapeia requisições GET para esse método
    public ResponseEntity<List<Cliente>> listar() {
        return ResponseEntity.ok(clienteService.listarTodos());
    } //retorna uma lista de todos os clientes

    @GetMapping("/{id}") //mapeia requisições GET com um ID específico
    public ResponseEntity<Cliente> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.buscarPorId(id));
    } //o @PathVariable indica que o ID virá da URL

    @PutMapping("/{id}") //mapeia requisições PUT com um ID específico
    public ResponseEntity<Cliente> atualizar(@PathVariable Long id, @RequestBody Cliente request) {
        Cliente atualizado = clienteService.atualizar(id, request);
        return ResponseEntity.ok(atualizado);
    } //atualiza os dados do cliente com o ID fornecido

    @DeleteMapping("/{id}") //mapeia requisições DELETE com um ID específico
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        clienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }//deleta o cliente com o ID fornecido
}



// controller recebe os pedidos (requisições HTTP) de clientes externos
// e decide qual "setor" (o ClienteService) deve cuidar da tarefa.
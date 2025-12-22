package com.example.demo.service;

import com.example.demo.model.Cliente;
import com.example.demo.repository.ClienteRepository;
import com.example.demo.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service // Indica que esta classe é um serviço do Spring
public class ClienteService {

    private final ClienteRepository clienteRepository; // Dependência do repositório de clientes

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    } // Injeção de dependência via construtor

    public Cliente criar(Cliente cliente) {
        return clienteRepository.save(cliente);
    } // Salva um novo cliente no banco de dados

    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado: " + id));
    } // Busca um cliente pelo ID, lança exceção se não encontrado
    //clienteRepository.findById(id): Tenta encontrar o cliente.(pode ou não ter o cliente).orElseThrow Se o cliente não for encontrado, ele lança a exceção NotFoundException que será capturada e tratada pelo seu RestAdvice

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    } // Retorna uma lista de todos os clientes

    public Cliente atualizar(Long id, Cliente update) {
        Cliente c = buscarPorId(id);
        if (update.getNome() != null) c.setNome(update.getNome());
        if (update.getEmail() != null) c.setEmail(update.getEmail());
        if (update.getTelefone() != null) c.setTelefone(update.getTelefone());
        if (update.getDocumento() != null) c.setDocumento(update.getDocumento());
        return clienteRepository.save(c);
    } // Atualiza os dados de um cliente existente parcial ous eja só um deles e retorna o cliente atualizado

    public void deletar(Long id) {
        Cliente c = buscarPorId(id);
        clienteRepository.delete(c);
    } // Deleta um cliente pelo ID
}

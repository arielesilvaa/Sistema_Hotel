package com.example.demo.service;

import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Cliente;
import com.example.demo.repository.ClienteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    private static final Logger logger = LoggerFactory.getLogger(ClienteService.class);

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente criar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado: " + id));
    }

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public Cliente atualizar(Long id, Cliente update) {
        Cliente c = buscarPorId(id);
        if (update.getNome() != null) c.setNome(update.getNome());
        if (update.getEmail() != null) c.setEmail(update.getEmail());
        if (update.getTelefone() != null) c.setTelefone(update.getTelefone());
        if (update.getDocumento() != null) c.setDocumento(update.getDocumento());
        return clienteRepository.save(c);
    }

    public void deletar(Long id) {
        Cliente c = buscarPorId(id);
        clienteRepository.delete(c);
    }
}

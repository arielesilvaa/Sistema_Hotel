package com.example.demo.service;

import com.example.demo.model.Quarto;
import com.example.demo.repository.QuartoRepository;
import com.example.demo.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service // Indica que esta classe é um serviço do Spring
@Transactional // Garante que as operações do banco de dados sejam transacionais ou salva tudo ou nada
public class QuartoService {

    private final QuartoRepository quartoRepository; // Dependência do repositório de quartos

    public QuartoService(QuartoRepository quartoRepository) {
        this.quartoRepository = quartoRepository;
    } // Injeção de dependência via construtor

    public Quarto criar(Quarto quarto) {
        return quartoRepository.save(quarto);
    } // Salva um novo quarto no banco de dados

    public Quarto buscarPorId(Long id) {
        return quartoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Quarto não encontrado: " + id));
    } // Busca um quarto pelo ID, lança exceção se não encontrado

    public List<Quarto> listarTodos() {
        return quartoRepository.findAll();
    } // Retorna uma lista de todos os quartos

    public Quarto atualizar(Long id, Quarto update) {
        Quarto q = buscarPorId(id);

        if (update.getNumero() != null) {
            q.setNumero(update.getNumero());
        }

        if (update.getCustoDiario() != null) {
            q.setCustoDiario(update.getCustoDiario());
        }

        if (update.getSuite() != null) { // CORREÇÃO 1: Usar getSuite() para Boolean Wrapper
            q.setSuite(update.getSuite()); // CORREÇÃO 2: Usar setSuite() para Boolean Wrapper
        }

        if (update.getNumeroCamas() != null) { // Melhor usar Wrapper Integer e checar null
            q.setNumeroCamas(update.getNumeroCamas());
        }

        // Se você tiver um campo possuiVaranda, ele deve seguir a mesma lógica:
        if (update.getPossuiVaranda() != null) {
            q.setPossuiVaranda(update.getPossuiVaranda());
        }

        return quartoRepository.save(q);
    }

    public Quarto atualizarValor(Long id, BigDecimal novoValor) {
        Quarto q = buscarPorId(id);
        q.setCustoDiario(novoValor);
        return quartoRepository.save(q);
    } // Atualiza apenas o valor do quarto com o ID fornecido

    public void deletar(Long id) {
        Quarto q = buscarPorId(id);
        quartoRepository.delete(q);
    } // Deleta um quarto pelo ID
}
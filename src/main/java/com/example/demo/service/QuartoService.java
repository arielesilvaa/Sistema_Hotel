package com.example.demo.service;

import com.example.demo.model.Quarto;
import com.example.demo.repository.QuartoRepository;
import com.example.demo.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class QuartoService {

    private final QuartoRepository quartoRepository;

    public QuartoService(QuartoRepository quartoRepository) {
        this.quartoRepository = quartoRepository;
    }

    public Quarto criar(Quarto quarto) {
        return quartoRepository.save(quarto);
    }

    public Quarto buscarPorId(Long id) {
        return quartoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Quarto não encontrado: " + id));
    }

    public List<Quarto> listarTodos() {
        return quartoRepository.findAll();
    }

    public Quarto atualizar(Long id, Quarto update) {
        Quarto q = buscarPorId(id);
        if (update.getNumero() != null) q.setNumero(update.getNumero());
        if (update.getCustoDiario() != null) q.setCustoDiario(update.getCustoDiario());
        q.setPossuiVaranda(update.isPossuiVaranda());
        q.setEhSuite(update.isEhSuite());
        q.setNumeroCamas(update.getNumeroCamas());
        return quartoRepository.save(q);
    }

    public Quarto atualizarValor(Long id, BigDecimal novoValor) {
        Quarto q = buscarPorId(id);
        q.setCustoDiario(novoValor);
        return quartoRepository.save(q);
    }

    public void deletar(Long id) {
        Quarto q = buscarPorId(id);
        quartoRepository.delete(q);
    }
}

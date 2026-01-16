package com.example.demo.service;

import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Quarto;
import com.example.demo.repository.QuartoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service

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

        if (update.getNumero() != null) {
            q.setNumero(update.getNumero());
        }

        if (update.getCustoDiario() != null) {
            q.setCustoDiario(update.getCustoDiario());
        }

        if (update.getSuite() != null) {
            q.setSuite(update.getSuite());
        }

        if (update.getNumeroCamas() != null) {
            q.setNumeroCamas(update.getNumeroCamas());
        }


        if (update.getPossuiVaranda() != null) {
            q.setPossuiVaranda(update.getPossuiVaranda());
        }

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

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
        //Recebe o id e valor
        Quarto q = buscarPorId(id); //Busca o quarto no banco de dados pelo id
        if (update.getNumero() != null) q.setNumero(update.getNumero()); // Atualização Parcial (String/BigDecimal): Para campos que podem ser nulos (como String ou BigDecimal), verifica se o novo valor foi enviado (!= null). Se sim, atualiza.
        if (update.getCustoDiario() != null) q.setCustoDiario(update.getCustoDiario()); // Aplicar Regra: Altera apenas o custo diário do objeto.
        q.setEhSuite(update.isEhSuite()); // Atualização Direta (boolean): Para campos booleanos, atualiza diretamente, pois eles sempre terão um valor (true ou false).
        q.setNumeroCamas(update.getNumeroCamas()); // Atualização Direta (int): Para campos inteiros, atualiza diretamente, pois eles sempre terão um valor.
        return quartoRepository.save(q); // Salva as alterações no banco de dados e retorna o quarto atualizado
    }

    public Quarto atualizarValor(Long id, BigDecimal novoValor) {
        Quarto q = buscarPorId(id); //Busca o quarto no banco de dados pelo id
        q.setCustoDiario(novoValor); //Altera apenas o custo diário do objeto.
        return quartoRepository.save(q); //Salva as alterações no banco de dados e retorna o quarto atualizado
    } // Atualiza apenas o valor do quarto com o ID fornecido

    public void deletar(Long id) {
        Quarto q = buscarPorId(id); //Busca o quarto no banco de dados pelo id
        quartoRepository.delete(q); //Deleta o quarto do banco de dados
    } // Deleta um quarto pelo ID
}



//Serviço é essencial porque ele lida com a lógica de negócio
package com.example.demo.service;

import com.example.demo.model.Pagamento;
import com.example.demo.model.Reserva;
import com.example.demo.enums.TipoPagamento;
import com.example.demo.repository.PagamentoRepository;
import com.example.demo.repository.ReservaRepository;
import com.example.demo.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service //Marca a classe como um componente de serviço que contém a lógica de negócio.
@Transactional //Garante que o método registrarPagamento será executado dentro de uma transação de banco de dados. Ou o pagamento é salvo, ou a operação falha completamente.
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository; // Dependência do repositório de pagamentos
    private final ReservaRepository reservaRepository; // Dependência do repositório de reservas

    public PagamentoService(PagamentoRepository pagamentoRepository, ReservaRepository reservaRepository) {
        this.pagamentoRepository = pagamentoRepository;
        this.reservaRepository = reservaRepository;
    } // Injeção de dependência via construtor

    public Pagamento registrarPagamento(Long reservaId, TipoPagamento tipo, java.math.BigDecimal valor) {
        // Recebe três informações essenciais para o pagamento: o ID da reserva que está sendo paga, o tipo de pagamento, e o valor pago. Ele retorna o objeto Pagamento salvo.
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new NotFoundException("Reserva não encontrada: " + reservaId));
        //Validação: buscar a Reserva no banco usando o reservaId. Se a reserva não for encontrada, ele lança uma exceção NotFoundException, que o
        // RestAdvice vai pegar e transformar em um Status 404 (Recurso Não Encontrado) para quem chamou a API.Se encontrada, ele guarda o objeto Reserva

        Pagamento pagamento = new Pagamento(); //Cria um novo objeto Pagamento vazio.
        pagamento.setReserva(reserva); //Liga o novo objeto Pagamento ao objeto Reserva que foi encontrado no banco (cria a chave estrangeira).
        pagamento.setTipo(tipo); //Define o tipo de pagamento.
        pagamento.setValorPago(valor); //Define o valor pago.
        pagamento.setDataPagamento(LocalDateTime.now()); //Define a data e hora do pagamento como o momento atual.

        return pagamentoRepository.save(pagamento); //Salva o objeto Pagamento no banco de dados e retorna o objeto salvo.
    }


}

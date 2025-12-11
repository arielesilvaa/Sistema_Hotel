package com.example.demo.service;

import com.example.demo.exception.CancelamentoNaoPermitidoException;
import com.example.demo.exception.InvalidDataException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.exception.QuartoOcupadoException;
import com.example.demo.exception.ReservaSobrepostaClienteException;
import com.example.demo.model.Cliente;
import com.example.demo.model.Quarto;
import com.example.demo.model.Reserva;
import com.example.demo.enums.StatusReserva;
import com.example.demo.enums.TipoPagamento;
import com.example.demo.repository.ClienteRepository;
import com.example.demo.repository.QuartoRepository;
import com.example.demo.repository.ReservaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service // Indica que é um componente de serviço
@Transactional // Garante que as operações do banco
public class ReservaService {

    private static final Logger logger = LoggerFactory.getLogger(ReservaService.class); // Logger para registrar mensagens útil para monitorar o que acontece no sistema e depurar erros (ex: quando o e-mail falhar).

    private final ReservaRepository reservaRepository; // Dependência do repositório de reservas
    private final ClienteRepository clienteRepository;
    private final QuartoRepository quartoRepository;
    private final EmailService emailService;


    private static final BigDecimal TAXA_SERVICO_PERCENT = new BigDecimal("0.10"); // 10%
    private static final BigDecimal ACRESCIMO_CREDITO = new BigDecimal("0.05"); // 5%
    private static final long HORAS_CANCELAMENTO = 48L; // 48 horas

    public ReservaService(ReservaRepository reservaRepository,
                          ClienteRepository clienteRepository,
                          QuartoRepository quartoRepository,
                          EmailService emailService) {
        this.reservaRepository = reservaRepository;
        this.clienteRepository = clienteRepository;
        this.quartoRepository = quartoRepository;
        this.emailService = emailService;
    } // Injeção de dependência via construtor

    public Reserva criarReserva(Long clienteId, Long quartoId, LocalDate dataCheckin, LocalDate dataCheckout, TipoPagamento tipoPagamento) {
        // validações básicas
        if (dataCheckin == null || dataCheckout == null || !dataCheckin.isBefore(dataCheckout)) {
            throw new InvalidDataException("Datas inválidas: checkin deve ser antes do checkout.");
        } //Validação de Datas 1: Checa se as datas são válidas (se Check-in é anterior ao Check-out). Se não, lança InvalidDataException
        if (dataCheckin.isBefore(LocalDate.now())) {
            throw new InvalidDataException("Data de checkin não pode ser no passado.");
        } //Validação de Datas 2: Impede que se crie reservas para o passado.

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado: " + clienteId));
        // valida cliente existe

        Quarto quarto = quartoRepository.findById(quartoId)
                .orElseThrow(() -> new NotFoundException("Quarto não encontrado: " + quartoId));
        // valida quarto existe

        // Verifica sobreposição de reservas no mesmo quarto (APENAS status ativos)
        List<StatusReserva> activeStatuses = List.of(StatusReserva.ABERTA, StatusReserva.CHECKIN); // Define os status considerados "ativos" para verificação de conflitos
        long conflitosQuarto = reservaRepository.countOverlappingReservationsForQuarto(quartoId, dataCheckin, dataCheckout, activeStatuses); // Conta quantas reservas ativas para o quarto se sobrepõem ao novo período.
        if (conflitosQuarto > 0) {
            throw new QuartoOcupadoException("Quarto já reservado nesse período.");
        } //Se houver conflitos, lança QuartoOcupadoException

        // Verifica sobreposição de reservas para o mesmo cliente
        long conflitosCliente = reservaRepository.countOverlappingReservationsForCliente(clienteId, dataCheckin, dataCheckout, activeStatuses); // Conta quantas reservas ativas do cliente se sobrepõem ao novo período.
        if (conflitosCliente > 0) {
            throw new ReservaSobrepostaClienteException("Cliente já possui reserva em período que se sobrepõe.");
        } //Se houver conflitos, lança ReservaSobrepostaClienteException

        // cálculo de valores
        long nDiarias = ChronoUnit.DAYS.between(dataCheckin, dataCheckout); // Calcula o número de diárias entre as datas de check-in e check-out
        if (nDiarias <= 0) {
            throw new InvalidDataException("Período inválido (1 ou mais diárias esperadas).");
        } // Validação adicional para garantir que haja pelo menos uma diária

        BigDecimal subtotal = quarto.getCustoDiario().multiply(BigDecimal.valueOf(nDiarias)); // Calcula o subtotal multiplicando o custo diário do quarto pelo número de diárias
        BigDecimal valorTaxaServico = subtotal.multiply(TAXA_SERVICO_PERCENT); // Calcula o valor da taxa de serviço (10% do subtotal)
        BigDecimal valorTotal = subtotal.add(valorTaxaServico); // Calcula o valor total somando o subtotal e a taxa de serviço

        if (tipoPagamento == TipoPagamento.CREDITO) {
            valorTotal = valorTotal.add(valorTotal.multiply(ACRESCIMO_CREDITO));
        } // Se o tipo de pagamento for cartão de crédito, adiciona um acréscimo de 5% ao valor total

        // Cria reserva
        Reserva reserva = new Reserva(); // Cria um novo objeto Reserva
        reserva.setCliente(cliente); // Define o cliente da reserva
        reserva.setQuarto(quarto); // Define o quarto da reserva
        reserva.setDataCheckin(dataCheckin); // Define a data de check-in
        reserva.setDataCheckout(dataCheckout); // Define a data de check-out
        reserva.setValorDiaria(quarto.getCustoDiario()); // Define o valor da diária com base no custo diário do quarto
        reserva.setValorTaxaServico(valorTaxaServico); // Define o valor da taxa de serviço
        reserva.setTipoPagamento(tipoPagamento); // Define o tipo de pagamento
        reserva.setValorTotal(valorTotal); // Define o valor total da reserva
        reserva.setStatus(StatusReserva.ABERTA); // Define o status inicial da reserva como ABERTA

        Reserva reservaSalva = reservaRepository.save(reserva); // Salva a reserva no banco de dados

        // Envia email (simulado)
        try {
            emailService.enviarEmailReserva(reservaSalva);
        } catch (Exception e) {
            logger.error("Falha ao enviar e-mail (simulado) para reserva {}: {}", reservaSalva.getId(), e.getMessage());
        } // Tenta enviar o e-mail de confirmação da reserva. Se falhar, registra o erro no log.

        return reservaSalva; // Retorna a reserva salva
    }

    public Reserva buscarPorId(Long id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Reserva não encontrada: " + id));
    } // Busca uma reserva pelo ID, lança exceção se não encontrado

    public List<Reserva> listarReservasDoCliente(Long clienteId) {
        // valida cliente existe
        if (!clienteRepository.existsById(clienteId)) {
            throw new NotFoundException("Cliente não encontrado: " + clienteId);
        } // Busca reservas do cliente ordenadas por data de check-in decrescente
        return reservaRepository.findByClienteIdOrderByDataCheckinDesc(clienteId);
        //(findByClienteIdOrderByDataCheckinDesc) para buscar as reservas daquele cliente ordenadas por data.
    }

    public Reserva cancelarReserva(Long reservaId) {
        Reserva reserva = buscarPorId(reservaId); // Busca a reserva pelo ID

        if (reserva.getStatus() != StatusReserva.ABERTA)  {
            throw new InvalidDataException("Só é possível cancelar reservas com status ABERTA.");
        } // Verifica se a reserva está no status ABERTA e só vai permitir cancelars e for aberta

        LocalDateTime agora = LocalDateTime.now(); // Obtém a data e hora atuais
        LocalDateTime checkinDateTime = reserva.getDataCheckin().atStartOfDay(); // Converte a data de check-in para LocalDateTime no início do dia
        long horasAntes = ChronoUnit.HOURS.between(agora, checkinDateTime); // Calcula a diferença em horas entre agora e o check-in
        if (horasAntes < HORAS_CANCELAMENTO) {
            throw new CancelamentoNaoPermitidoException("Cancelamento permitido apenas com mínimo de 48 horas antes do checkin.");
        } // Verifica se o cancelamento está sendo feito com pelo menos 48 horas de antecedência

        reserva.setStatus(StatusReserva.CANCELADA);
        return reservaRepository.save(reserva);
        // Atualiza o status da reserva para CANCELADA e salva no banco de dados
    }

    public Reserva realizarCheckin(Long reservaId) {
        Reserva reserva = buscarPorId(reservaId); // Busca a reserva pelo ID

        if (reserva.getStatus() != StatusReserva.ABERTA) {
            throw new InvalidDataException("Somente reservas com status ABERTA podem fazer checkin.");
        } // Verifica se a reserva está no status ABERTA

        LocalDate hoje = LocalDate.now(); // Obtém a data atual
        if (hoje.isBefore(reserva.getDataCheckin()) || hoje.isAfter(reserva.getDataCheckout().minusDays(1))) {
            throw new InvalidDataException("Checkin só permitido no período da reserva.");
        } // Verifica se a data atual está dentro do período permitido para check-in

        reserva.setStatus(StatusReserva.CHECKIN); // Atualiza o status da reserva para CHECKIN
        reserva.setDataHoraEntrada(LocalDateTime.now()); // Define a data e hora de entrada como o momento atual
        return reservaRepository.save(reserva); // Salva a alteração no banco de dados e retorna a reserva atualizada
    }

    public Reserva realizarCheckout(Long reservaId) {
        Reserva reserva = buscarPorId(reservaId); // Busca a reserva pelo ID

        if (reserva.getStatus() != StatusReserva.CHECKIN) {
            throw new InvalidDataException("Somente reservas com status CHECKIN podem fazer checkout.");
        } // Verifica se a reserva está no status CHECKIN

        reserva.setStatus(StatusReserva.CHECKOUT); // Atualiza o status da reserva para CHECKOUT
        reserva.setDataHoraFinalizacao(LocalDateTime.now()); // Define a data e hora de finalização como o momento atual
        return reservaRepository.save(reserva); // Salva a alteração no banco de dados e retorna a reserva atualizada
    }


    public List<Reserva> encontrarReservasExpiradas() {
        LocalDate hoje = LocalDate.now();
        return reservaRepository.findExpiredReservations(StatusReserva.ABERTA, hoje);
    } // Encontra reservas que estão expiradas (status ABERTA e dataCheckin no passado)

    public Reserva marcarComoExpirada(Long reservaId) {
        Reserva r = buscarPorId(reservaId); // Busca a reserva pelo ID
        if (r.getStatus() == StatusReserva.ABERTA && r.getDataCheckin().isBefore(LocalDate.now())) {
            r.setStatus(StatusReserva.EXPIRADA); // Atualiza o status da reserva para EXPIRADA
            return reservaRepository.save(r); // Salva a alteração no banco de dados e retorna a reserva atualizada
        } else {
            throw new InvalidDataException("Reserva não pode ser marcada como expirada.");
        } // Marca a reserva como EXPIRADA se aplicável
    }
}

// reservaservice proxy = aplication getbean serve para pegar o bean do spring que é o reservaservice
// ben é o objeto gerenciado pelo spring que tem todas as funcionalidades
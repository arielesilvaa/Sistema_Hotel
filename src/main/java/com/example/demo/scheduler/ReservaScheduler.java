package com.example.demo.scheduler;

import com.example.demo.enums.StatusReserva;
import com.example.demo.model.Cliente;
import com.example.demo.model.Reserva;
import com.example.demo.repository.ClienteRepository;
import com.example.demo.repository.ReservaRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

// CORREÇÃO DAS IMPORTAÇÕES: Use apenas SLF4J
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component // Indica que esta classe é um componente gerenciado pelo Spring
public class ReservaScheduler {

    // CORREÇÃO DA INICIALIZAÇÃO: Sem o cast (Logger)
    private static final Logger logger = LoggerFactory.getLogger(ReservaScheduler.class);

    private final ReservaRepository reservaRepository;
    private final ClienteRepository clienteRepository;

    public ReservaScheduler(ReservaRepository reservaRepository, ClienteRepository clienteRepository) {
        this.reservaRepository = reservaRepository;
        this.clienteRepository = clienteRepository;
    }

    @Scheduled(fixedDelay = 30000) // Executa a cada 30 segundos
    public void verificarReservasExpiradas() {
        logger.info("--- Executando verificação de reservas expiradas ---");

        LocalDateTime dataHoraCorte = LocalDateTime.now().minusMinutes(1); // Considera reservas com check-in antes de 1 minuto atrás
        // O SLF4J usa {} como placeholder para variáveis
        logger.info("Hora de Corte (Check-in deve ser anterior a): {}", dataHoraCorte);

        List<Reserva> reservasParaExpirar = reservaRepository.findByStatusInAndDataCheckinBefore(
                List.of(StatusReserva.ABERTA),
                dataHoraCorte
        );
        logger.info("Encontradas {} reservas Abertas Para Expirar.", reservasParaExpirar.size());

        expirarReservas(reservasParaExpirar);
        bloquearClientesNoShow(reservasParaExpirar);

        logger.info("--- Verificação concluída. ---");
    }

    private void expirarReservas(List<Reserva> reservas) {
        for (Reserva reserva : reservas) {

            logger.warn("[AÇÃO] Reserva ID {} expirada (No-Show). Status anterior: {}", reserva.getId(), reserva.getStatus());
            reserva.setStatus(StatusReserva.EXPIRADA); // Atualiza o status da reserva para EXPIRADA
            reservaRepository.save(reserva); // Salva a atualização no banco de dados

            }
        }

        private void bloquearClientesNoShow(List<Reserva> reservas) {
        for (Reserva reserva : reservas) {
            Cliente cliente = reserva.getCliente();
            if (!cliente.isBloqueado()) { // Verifica se o cliente já não está bloqueado
                cliente.setBloqueado(true); // Bloqueia o cliente
                clienteRepository.save(cliente); // Salva a atualização no banco de dados
                logger.warn("Cliente ID {} bloqueado por No-Show na Reserva ID {}.", cliente.getId(), reserva.getId());
            }
        }
    }
}
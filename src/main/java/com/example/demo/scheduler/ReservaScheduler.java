package com.example.demo.scheduler;

import com.example.demo.enums.StatusReserva;
import com.example.demo.model.Cliente;
import com.example.demo.model.Reserva;
import com.example.demo.repository.ClienteRepository;
import com.example.demo.repository.ReservaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ReservaScheduler {

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

        var dataHoraCorte = LocalDateTime.now().minusMinutes(1);

        logger.info("Hora de Corte (Check-in deve ser anterior a): {}", dataHoraCorte);

        var reservasParaExpirar = reservaRepository.findByStatusInAndDataCheckinBefore(
                List.of(StatusReserva.ABERTA),
                dataHoraCorte
        );
        logger.info("Encontradas {} reservas Abertas Para Expirar.", reservasParaExpirar.size());

        expirarReservas(reservasParaExpirar);
        bloquearClientesNoShow(reservasParaExpirar);

        logger.debug("--- Verificação concluída. ---");
    }

    private void expirarReservas(List<Reserva> reservas) {
        for (Reserva reserva : reservas) {
            logger.warn("[AÇÃO] Reserva ID {} expirada (No-Show). Status anterior: {}", reserva.getId(), reserva.getStatus());
            reserva.setStatus(StatusReserva.EXPIRADA);
            reservaRepository.save(reserva);

        }
    }

    private void bloquearClientesNoShow(List<Reserva> reservas) {
        for (Reserva reserva : reservas) {
            Cliente cliente = reserva.getCliente();
            if (!cliente.isBloqueado()) {
                cliente.setBloqueado(true);
                clienteRepository.save(cliente);
                logger.info("Cliente ID {} bloqueado por No-Show na Reserva ID {}.", cliente.getId(), reserva.getId());
            }
        }
    }
}


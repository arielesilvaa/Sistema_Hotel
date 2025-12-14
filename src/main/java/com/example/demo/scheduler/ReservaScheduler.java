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
import org.springframework.transaction.annotation.Transactional;

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

    // Roda a cada 30 segundos (para testes)
    @Scheduled(initialDelay = 30000, fixedRate = 30000)
    @Transactional
    public void verificarReservasNaoHonradas() {

        LocalDateTime dataHoraCorte = LocalDateTime.now();
        logger.info("Iniciando verificação de reservas para expirar. Horário de corte: {}", dataHoraCorte);

        // CORREÇÃO: Usando o método correto definido no Repositório
        List<Reserva> reservasParaExpirar = reservaRepository.findByStatusAndDataCheckinBefore(StatusReserva.ABERTA, dataHoraCorte);

        if (reservasParaExpirar.isEmpty()) {
            logger.info("Nenhuma reserva elegível para expiração encontrada.");
            return;
        }

        int contador = 0;
        for (Reserva reserva : reservasParaExpirar) {

            // 1. Atualizar a Reserva para EXPIRADA (No-Show)
            reserva.setStatus(StatusReserva.EXPIRADA);
            reserva.setDataHoraFinalizacao(LocalDateTime.now());
            reservaRepository.save(reserva);

            // 2. Bloquear o Cliente
            Cliente cliente = reserva.getCliente();

            // Verificação de segurança e bloqueio
            if (cliente != null && !cliente.isBloqueado()) {
                cliente.setBloqueado(true);
                clienteRepository.save(cliente);
                logger.warn("Cliente ID {} bloqueado por No-Show (Reserva ID {}).", cliente.getId(), reserva.getId());
            }

            contador++;
        }

        logger.info("Processo concluído. Total de {} reservas expiradas (No-Show).", contador);
    }
}
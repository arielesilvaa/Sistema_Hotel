package com.example.demo.scheduler;

import com.example.demo.enums.StatusReserva;
import com.example.demo.model.Cliente;
import com.example.demo.model.Reserva;
import com.example.demo.repository.ClienteRepository;
import com.example.demo.repository.ReservaRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ReservaScheduler {

    private final ReservaRepository reservaRepository;
    private final ClienteRepository clienteRepository;

    public ReservaScheduler(ReservaRepository reservaRepository, ClienteRepository clienteRepository) {
        this.reservaRepository = reservaRepository;
        this.clienteRepository = clienteRepository;
    }

    // Agendado para rodar a cada 30 minutos (1800000 ms)
    @Scheduled(fixedDelay = 1800000)
    @Transactional
    public void verificarReservasExpiradas() {
        System.out.println("--- Executando verificação de reservas expiradas ---");

        // Regra de Corte: Data e hora anterior a 1 minuto atrás.
        // **IMPORTANTE**: Reverter para minusHours(12) após o teste!
        LocalDateTime dataHoraCorte = LocalDateTime.now().minusMinutes(1);
        System.out.println("DEBUG: Hora de Corte (Check-in deve ser anterior a): " + dataHoraCorte);

        // 1. Buscando ABERTAS (Se expirar, bloqueia o cliente)
        List<Reserva> reservasAbertasParaExpirar = reservaRepository.findByStatusAndDataCheckinBefore(
                StatusReserva.ABERTA,
                dataHoraCorte
        );
        System.out.println("DEBUG: Encontradas " + reservasAbertasParaExpirar.size() + " reservas ABERTAS para expirar.");


        // 2. Buscando PAGAS (Se expirar, não bloqueia o cliente)
        List<Reserva> reservasPagasParaExpirar = reservaRepository.findByStatusAndDataCheckinBefore(
                StatusReserva.PAGA,
                dataHoraCorte
        );
        System.out.println("DEBUG: Encontradas " + reservasPagasParaExpirar.size() + " reservas PAGAS para expirar.");

        // Processamento
        processarExpiracao(reservasAbertasParaExpirar, true);

        processarExpiracao(reservasPagasParaExpirar, false);

        System.out.println("--- Verificação concluída. ---");
    }

    private void processarExpiracao(List<Reserva> reservas, boolean deveBloquearCliente) {
        for (Reserva reserva : reservas) {
            System.out.println("--- [AÇÃO] Reserva ID " + reserva.getId() + " expirada (No-Show). Status anterior: " + reserva.getStatus() + " ---");

            // Muda o status da reserva
            reserva.setStatus(StatusReserva.EXPIRADA);
            reservaRepository.save(reserva);
            // Em testes extremos, pode ser necessário um flush: reservaRepository.flush();

            if (deveBloquearCliente) {
                // Bloqueia o cliente (apenas se a reserva era ABERTA)
                Cliente cliente = reserva.getCliente();
                if (!cliente.isBloqueado()) {
                    cliente.setBloqueado(true);
                    clienteRepository.save(cliente);
                    System.out.println("Cliente ID " + cliente.getId() + " bloqueado por No-Show.");
                }
            }
        }
    }
}
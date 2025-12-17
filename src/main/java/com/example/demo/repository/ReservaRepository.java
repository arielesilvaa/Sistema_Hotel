package com.example.demo.repository;

import com.example.demo.enums.StatusReserva;
import com.example.demo.model.Cliente;
import com.example.demo.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    // 1. Busca por Cliente (Usado no ReservaService)
    List<Reserva> findByCliente(Cliente cliente);

    // 2. Método para o Scheduler: Busca reservas em status específico cuja data de check-in já passou.
    // USANDO @Query EXPLÍCITO para contornar problemas de precisão de tempo (Timestamp) com findBy...Before
    @Query("SELECT r FROM Reserva r WHERE r.status = :status AND r.dataCheckin < :dataLimite")
    List<Reserva> findByStatusAndDataCheckinBefore(
            @Param("status") StatusReserva status,
            @Param("dataLimite") LocalDateTime dataLimite
    );

    // 3. Validação de Conflito de Datas (Quarto Ocupado)
    @Query("SELECT COUNT(r) FROM Reserva r WHERE r.quarto.id = :quartoId " +
            "AND r.status IN :activeStatuses " +
            "AND ( (r.dataCheckin < :checkout AND r.dataCheckout > :checkin) )")
    long countOverlappingReservationsForQuarto(@Param("quartoId") Long quartoId,
                                               @Param("checkin") LocalDateTime checkin,
                                               @Param("checkout") LocalDateTime checkout,
                                               @Param("activeStatuses") List<StatusReserva> activeStatuses);

    // 4. Validação de Conflito de Datas (Cliente com Reserva Ativa)
    @Query("SELECT COUNT(r) FROM Reserva r WHERE r.cliente.id = :clienteId " +
            "AND r.status IN :activeStatuses " +
            "AND ( (r.dataCheckin < :checkout AND r.dataCheckout > :checkin) )")
    long countOverlappingReservationsForCliente(@Param("clienteId") Long clienteId,
                                                @Param("checkin") LocalDateTime checkin,
                                                @Param("checkout") LocalDateTime checkout,
                                                @Param("activeStatuses") List<StatusReserva> activeStatuses);
}
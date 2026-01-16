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

    List<Reserva> findByCliente(Cliente cliente);

    @Query("SELECT r FROM Reserva r WHERE r.status IN :statuses AND r.dataCheckin < :dataLimite")

    List<Reserva> findByStatusInAndDataCheckinBefore(
            @Param("statuses") List<StatusReserva> statuses,
            @Param("dataLimite") LocalDateTime dataLimite
    );

    @Query("SELECT COUNT(r) FROM Reserva r WHERE r.quarto.id = :quartoId " +
            "AND r.status IN :activeStatuses " +
            "AND ( (r.dataCheckin < :checkout AND r.dataCheckout > :checkin) )")
    long countOverlappingReservationsForQuarto(@Param("quartoId") Long quartoId,
                                               @Param("checkin") LocalDateTime checkin,
                                               @Param("checkout") LocalDateTime checkout,
                                               @Param("activeStatuses") List<StatusReserva> activeStatuses);

    @Query("SELECT COUNT(r) FROM Reserva r WHERE r.cliente.id = :clienteId " +
            "AND r.status IN :activeStatuses " +
            "AND ( (r.dataCheckin < :checkout AND r.dataCheckout > :checkin) )")
    long countOverlappingReservationsForCliente(@Param("clienteId") Long clienteId,
                                                @Param("checkin") LocalDateTime checkin,
                                                @Param("checkout") LocalDateTime checkout,
                                                @Param("activeStatuses") List<StatusReserva> activeStatuses);
}


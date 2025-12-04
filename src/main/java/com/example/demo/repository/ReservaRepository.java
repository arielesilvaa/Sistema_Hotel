package com.example.demo.repository;

import com.example.demo.model.Reserva;
import com.example.demo.enums.StatusReserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByClienteIdOrderByDataCheckinDesc(Long clienteId);
    @Query("SELECT COUNT(r) FROM Reserva r WHERE r.quarto.id = :quartoId AND r.status IN :activeStatuses AND r.dataCheckin < :dataCheckout AND r.dataCheckout > :dataCheckin")

    long countOverlappingReservationsForQuarto(
            @Param("quartoId") Long quartoId,
            @Param("dataCheckin") LocalDate dataCheckin,
            @Param("dataCheckout") LocalDate dataCheckout,
            @Param("activeStatuses") List<StatusReserva> activeStatuses
    );

    @Query("SELECT COUNT(r) FROM Reserva r WHERE r.cliente.id = :clienteId AND r.status IN :activeStatuses AND r.dataCheckin < :dataCheckout AND r.dataCheckout > :dataCheckin")

    long countOverlappingReservationsForCliente(
            @Param("clienteId") Long clienteId,
            @Param("dataCheckin") LocalDate dataCheckin,
            @Param("dataCheckout") LocalDate dataCheckout,
            @Param("activeStatuses") List<StatusReserva> activeStatuses
    );

    @Query("SELECT r FROM Reserva r WHERE r.status = :status AND r.dataCheckin < :today")
    List<Reserva> findExpiredReservations(@Param("status") StatusReserva status, @Param("today") LocalDate today);
}
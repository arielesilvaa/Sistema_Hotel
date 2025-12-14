package com.example.demo.repository;

import com.example.demo.enums.StatusReserva;
import com.example.demo.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    // Método usado pelo Scheduler (Spring Data JPA Query Method)
    List<Reserva> findByStatusAndDataCheckinBefore(StatusReserva status, LocalDateTime dataCheckin);

    // Queries de conflito (Quarto e Cliente)
    @Query("SELECT COUNT(r) FROM Reserva r WHERE " +
            "r.quarto.id = :quartoId AND " +
            "r.status IN :activeStatuses AND " +
            "(" +
            "(r.dataCheckin < :checkoutDateTime) AND (:checkinDateTime < r.dataCheckout)" +
            ")")
    long countOverlappingReservationsForQuarto(
            @Param("quartoId") Long quartoId,
            @Param("checkinDateTime") LocalDateTime checkinDateTime,
            @Param("checkoutDateTime") LocalDateTime checkoutDateTime,
            @Param("activeStatuses") List<StatusReserva> activeStatuses
    );

    @Query("SELECT COUNT(r) FROM Reserva r WHERE " +
            "r.cliente.id = :clienteId AND " +
            "r.status IN :activeStatuses AND " +
            "(" +
            "(r.dataCheckin < :checkoutDateTime) AND (:checkinDateTime < r.dataCheckout)" +
            ")")
    long countOverlappingReservationsForCliente(
            @Param("clienteId") Long clienteId,
            @Param("checkinDateTime") LocalDateTime checkinDateTime,
            @Param("checkoutDateTime") LocalDateTime checkoutDateTime,
            @Param("activeStatuses") List<StatusReserva> activeStatuses
    );

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN TRUE ELSE FALSE END FROM Reserva r WHERE r.cliente.id = :clienteId AND r.status = 'EXPIRADA'")
    boolean clientePossuiReservaExpirada(@Param("clienteId") Long clienteId);
}
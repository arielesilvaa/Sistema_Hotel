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

    // 1. Busca por Cliente
    List<Reserva> findByCliente(Cliente cliente);


    // 2. MÉTODO ATUALIZADO: Agora usa "IN" e aceita uma List de Status
    @Query("SELECT r FROM Reserva r WHERE r.status IN :statuses AND r.dataCheckin < :dataLimite")
    List<Reserva> findByStatusInAndDataCheckinBefore(
            @Param("statuses") List<StatusReserva> statuses,
            @Param("dataLimite") LocalDateTime dataLimite
    );

    // 3. Validação de Conflito (Quarto)
    @Query("SELECT COUNT(r) FROM Reserva r WHERE r.quarto.id = :quartoId " +
            "AND r.status IN :activeStatuses " +
            "AND ( (r.dataCheckin < :checkout AND r.dataCheckout > :checkin) )")
    long countOverlappingReservationsForQuarto(@Param("quartoId") Long quartoId,
                                               @Param("checkin") LocalDateTime checkin,
                                               @Param("checkout") LocalDateTime checkout,
                                               @Param("activeStatuses") List<StatusReserva> activeStatuses);

    // 4. Validação de Conflito (Cliente)
    @Query("SELECT COUNT(r) FROM Reserva r WHERE r.cliente.id = :clienteId " +
            "AND r.status IN :activeStatuses " +
            "AND ( (r.dataCheckin < :checkout AND r.dataCheckout > :checkin) )")
    long countOverlappingReservationsForCliente(@Param("clienteId") Long clienteId,
                                                @Param("checkin") LocalDateTime checkin,
                                                @Param("checkout") LocalDateTime checkout,
                                                @Param("activeStatuses") List<StatusReserva> activeStatuses);
}
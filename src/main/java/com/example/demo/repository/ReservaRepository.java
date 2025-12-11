package com.example.demo.repository;

import com.example.demo.model.Reserva;
import com.example.demo.enums.StatusReserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository // Indica que esta interface é um repositório Spring Data JPA
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByClienteIdOrderByDataCheckinDesc(Long clienteId); // Busca reservas por ID do cliente, ordenadas por data de check-in decrescente mais recente pro menos recente
    @Query("SELECT COUNT(r) FROM Reserva r WHERE r.quarto.id = :quartoId AND r.status IN :activeStatuses AND r.dataCheckin < :dataCheckout AND r.dataCheckout > :dataCheckin")
    //@Query permite escrever a consulta usando JPQL
    //Condição 1: A reserva deve ser para o quarto com o ID (:quartoId).
    //Condição 2: O status da reserva deve estar na lista de status ativos (ex: ABERTA, CHECKIN)
    //Condição 3: lógica de sobreposição datas. Garante que a data de início de uma é antes da data de fim da outra, e a data de fim de uma é depois da data de início da outra.

    long countOverlappingReservationsForQuarto(
            @Param("quartoId") Long quartoId, // @Param garantem que o Spring passe os valores corretos (quartoId, dataCheckin, etc.) para a consulta JPQL.
            @Param("dataCheckin") LocalDate dataCheckin,
            @Param("dataCheckout") LocalDate dataCheckout,
            @Param("activeStatuses") List<StatusReserva> activeStatuses
    ); // Define o método que, quando chamado, executa a consulta acima.

    @Query("SELECT COUNT(r) FROM Reserva r WHERE r.cliente.id = :clienteId AND r.status IN :activeStatuses AND r.dataCheckin < :dataCheckout AND r.dataCheckout > :dataCheckin")
    // mas a primeira condição verifica o r.cliente.id em vez do r.quarto.id.

    long countOverlappingReservationsForCliente(
            @Param("clienteId") Long clienteId,
            @Param("dataCheckin") LocalDate dataCheckin,
            @Param("dataCheckout") LocalDate dataCheckout,
            @Param("activeStatuses") List<StatusReserva> activeStatuses
    ); // Conta quantas reservas ativas do cliente se sobrepõem ao novo período.

    @Query("SELECT r FROM Reserva r WHERE r.status = :status AND r.dataCheckin < :today")
    //Busca todas as Reservas com o status (ABERTA) e se a dataCheckin já passou em relação à data de hoje (:today).
    List<Reserva> findExpiredReservations(@Param("status") StatusReserva status, @Param("today") LocalDate today);
    //Retorna uma lista de objetos Reserva que se encaixam nos critérios de expiração.
}
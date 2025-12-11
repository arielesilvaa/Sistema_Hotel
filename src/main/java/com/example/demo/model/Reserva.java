package com.example.demo.model;

import com.example.demo.enums.StatusReserva;
import com.example.demo.enums.TipoPagamento;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.FetchType;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity // Indica que esta classe é uma entidade JPA
@Table(name = "reservas") // Mapeia a entidade para a tabela "reservas"
@Getter //  Substitui todos os getters
@Setter //  Substitui todos os setters
@NoArgsConstructor //  Gera o construtor vazio (necessário para JPA)
@AllArgsConstructor // Gera o construtor com todos os 8 campos (resolve o alerta)
public class Reserva extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER) // Muitas reservas para um cliente, fetch = FetchType.EAGER: fala JPA para carregar o Cliente junto quando buscada a Reserva
    @JoinColumn(name = "cliente_id", nullable = false) // Chave estrangeira para a tabela clientes não pode ser nula
    @JsonIgnore // evita serializar cliente inteiro automaticamente
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.EAGER) // Muitas reservas para um quarto,Garante que toda reserva precisa de um quarto e carrega o quarto junto (EAGER).
    @JoinColumn(name = "quarto_id", nullable = false) // Chave estrangeira para a tabela quartos não pode ser nula
    @JsonIgnore // evita serializar quarto inteiro automaticamente
    private Quarto quarto;

    @Column(nullable = false) // Data de check-in não pode ser nula
    private LocalDate dataCheckin; // Data de check-in

    @Column(nullable = false) // Data de check-out não pode ser nula
    private LocalDate dataCheckout; // Data de check-out

    @Column(nullable = false) // Valor da diária não pode ser nulo
    private BigDecimal valorDiaria; // Valor da diária

    @Column(nullable = false) // Valor da taxa de serviço não pode ser nulo
    private BigDecimal valorTaxaServico; // Valor da taxa de serviço

    @Enumerated(EnumType.STRING) // Armazena o enum como string no banco de dados
    @Column(nullable = false) // Tipo de pagamento não pode ser nulo
    private TipoPagamento tipoPagamento; // Tipo de pagamento

    @Column(nullable = false) // Valor total não pode ser nulo
    private BigDecimal valorTotal; // Valor total da reserva

    @Enumerated(EnumType.STRING) // Armazena o enum como string no banco de dados
    @Column(nullable = false) // Status da reserva não pode ser nulo
    private StatusReserva status = StatusReserva.ABERTA; // Status da reserva (padrão: ABERTA)

    private LocalDateTime dataHoraEntrada; // Data e hora de entrada na reserva
    private LocalDateTime dataHoraFinalizacao; // Data e hora de finalização da reserva

}
package com.example.demo.mapper;

import com.example.demo.dto.ClienteDTO;
import com.example.demo.dto.QuartoDTO;
import com.example.demo.dto.ReservaResponseDTO;
import com.example.demo.model.Cliente;
import com.example.demo.model.Quarto;
import com.example.demo.model.Reserva;
import org.springframework.stereotype.Component;

@Component
public class ReservaMapper {

    public ReservaResponseDTO toReservaResponseDTO(Reserva r) {
        if (r == null) return null;

        ReservaResponseDTO dto = new ReservaResponseDTO();
        dto.setId(r.getId());
        dto.setClienteId(r.getCliente() != null ? r.getCliente().getId() : null);
        dto.setQuartoId(r.getQuarto() != null ? r.getQuarto().getId() : null);

        dto.setDataCheckin(r.getDataCheckin());
        dto.setDataCheckout(r.getDataCheckout());

        dto.setValorDiaria(r.getValorDiaria());
        dto.setValorTaxaServico(r.getValorTaxaServico());
        dto.setValorTotal(r.getValorTotal());

        dto.setTipoPagamento(r.getTipoPagamento());
        dto.setStatus(r.getStatus());

        dto.setDataHoraEntrada(r.getDataHoraEntrada());
        dto.setDataHoraFinalizacao(r.getDataHoraFinalizacao());

        return dto;
    }

    public ClienteDTO toClienteDTO(Cliente c) {
        if (c == null) return null;
        ClienteDTO dto = new ClienteDTO();
        dto.setId(c.getId());
        dto.setNome(c.getNome());
        dto.setEmail(c.getEmail());
        dto.setTelefone(c.getTelefone());
        dto.setDocumento(c.getDocumento());
        return dto;
    }

    public QuartoDTO toQuartoDTO(Quarto q) {
        if (q == null) return null;
        QuartoDTO dto = new QuartoDTO();
        dto.setId(q.getId());
        dto.setNumero(q.getNumero());
        dto.setCustoDiario(q.getCustoDiario());
        dto.setPossuiVaranda(q.isPossuiVaranda());
        dto.setEhSuite(q.isEhSuite());
        dto.setNumeroCamas(q.getNumeroCamas());
        return dto;
    }

    public Cliente toClienteEntity(ClienteDTO dto) {
        if (dto == null) return null;
        Cliente c = new Cliente();
        c.setNome(dto.getNome());
        c.setEmail(dto.getEmail());
        c.setTelefone(dto.getTelefone());
        c.setDocumento(dto.getDocumento());
        // NÃO setamos id aqui (gerenciado pelo JPA) — se quiser atualizar, o service busca a entidade e copia campos.
        return c;
    }

    public Quarto toQuartoEntity(QuartoDTO dto) {
        if (dto == null) return null;
        Quarto q = new Quarto();
        q.setNumero(dto.getNumero());
        q.setCustoDiario(dto.getCustoDiario());
        q.setPossuiVaranda(dto.isPossuiVaranda());
        q.setEhSuite(dto.isEhSuite());
        q.setNumeroCamas(dto.getNumeroCamas());
        return q;
    }
}

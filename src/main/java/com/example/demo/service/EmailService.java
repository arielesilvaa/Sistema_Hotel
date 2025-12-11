package com.example.demo.service;

import com.example.demo.model.Reserva;

public interface EmailService {
    void enviarEmailReserva(Reserva reserva);
}


//ele não retorna nada mas é responsavel por enviar email quando uma reserva é feita
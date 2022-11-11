package com.example.eunboard.domain.repository.ticket;

import com.example.eunboard.domain.entity.Ticket;

import java.util.List;

public interface CustomTicketRepository {
    public List<Ticket> findAll();
    public Boolean existTicket(Long memberId);
}

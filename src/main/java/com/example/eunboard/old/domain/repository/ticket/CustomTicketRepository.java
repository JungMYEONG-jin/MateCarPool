package com.example.eunboard.old.domain.repository.ticket;

import com.example.eunboard.old.domain.entity.Ticket;

import java.util.List;

public interface CustomTicketRepository {
    public List<Ticket> findAll();
    public Boolean existTicket(Long memberId);
}

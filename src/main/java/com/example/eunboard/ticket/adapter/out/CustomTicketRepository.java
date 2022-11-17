package com.example.eunboard.ticket.adapter.out;

import com.example.eunboard.ticket.domain.Ticket;

import java.util.List;

public interface CustomTicketRepository {
     List<Ticket> findAll();
     Boolean existTicket(Long memberId);
}

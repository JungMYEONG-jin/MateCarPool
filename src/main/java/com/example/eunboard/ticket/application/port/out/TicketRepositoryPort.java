package com.example.eunboard.ticket.application.port.out;

import com.example.eunboard.member.domain.Member;
import com.example.eunboard.ticket.domain.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketRepositoryPort {
    Optional<Ticket> findById(Long id);
    Ticket save(Ticket ticket);
    Ticket findByMember(Member member);
    List<Ticket> findAll();
    Boolean existTicket(Long memberId);
}

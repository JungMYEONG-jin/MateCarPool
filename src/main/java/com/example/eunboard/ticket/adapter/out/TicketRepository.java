package com.example.eunboard.ticket.adapter.out;

import com.example.eunboard.member.domain.Member;
import com.example.eunboard.ticket.domain.Ticket;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long>,CustomTicketRepository {
    Ticket findByMember(Member member);
}
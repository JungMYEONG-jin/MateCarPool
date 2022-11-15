package com.example.eunboard.domain.repository.ticket;

import com.example.eunboard.member.domain.Member;
import com.example.eunboard.domain.entity.Ticket;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long>,CustomTicketRepository {

    Ticket findByMember(Member member);
}
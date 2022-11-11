package com.example.eunboard.domain.repository.ticket;

import com.example.eunboard.domain.entity.Member;
import com.example.eunboard.domain.entity.Ticket;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long>,CustomTicketRepository {

    Ticket findByMember(Member member);
}
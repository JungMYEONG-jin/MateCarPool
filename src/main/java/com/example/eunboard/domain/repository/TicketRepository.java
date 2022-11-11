package com.example.eunboard.domain.repository;

import com.example.eunboard.domain.entity.Member;
import com.example.eunboard.domain.entity.Ticket;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Ticket findByMember(Member member);
}
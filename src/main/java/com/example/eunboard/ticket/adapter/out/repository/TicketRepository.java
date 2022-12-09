package com.example.eunboard.ticket.adapter.out.repository;

import com.example.eunboard.member.domain.Member;
import com.example.eunboard.ticket.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long>,CustomTicketRepository {
    Ticket findByMember(Member member);
    List<Ticket> findByMember_MemberId(Long memberId);
}
package com.example.eunboard.ticket.adapter.out;

import com.example.eunboard.member.domain.Member;
import com.example.eunboard.ticket.application.port.out.TicketRepositoryPort;
import com.example.eunboard.ticket.domain.Ticket;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class TicketRepositoryAdapter implements TicketRepositoryPort {

    private final TicketRepository ticketRepository;

    @Override
    public Optional<Ticket> findById(Long id) {
        return ticketRepository.findById(id);
    }

    @Override
    public Ticket save(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket findByMember(Member member) {
        return ticketRepository.findByMember(member);
    }

    @Override
    public List<Ticket> findAll() {
        return ticketRepository.findAll();
    }

    @Override
    public Boolean existTicket(Long memberId) {
        return ticketRepository.existTicket(memberId);
    }
}

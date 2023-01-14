package com.example.eunboard.passenger.adapter.out.repository;

import com.example.eunboard.member.domain.Member;
import com.example.eunboard.passenger.application.port.out.PassengerRepositoryPort;
import com.example.eunboard.passenger.domain.Passenger;
import com.example.eunboard.ticket.domain.Ticket;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PassengerRepositoryAdapter implements PassengerRepositoryPort {

    private final PassengerRepository passengerRepository;

    @Override
    public Passenger save(Passenger passenger) {
        return passengerRepository.save(passenger);
    }

    @Override
    public boolean findRide(Passenger entity) {
        return passengerRepository.findRide(entity);
    }

    @Override
    public Passenger findMyPassenger(Passenger entity) {
        return passengerRepository.findMyPassenger(entity);
    }

    @Override
    public List<Passenger> getBoardingList(Long memberId) {
        return passengerRepository.getBoardingList(memberId);
    }

    @Override
    public boolean existBoardingStatePassenger(Long memberId) {
        return passengerRepository.existBoardingStatePassenger(memberId);
    }

    @Override
    public Passenger findCurrentPassengerByMember(Member member) {
        return passengerRepository.findCurrentPassengerByMember(member);
    }

    @Override
    public List<Passenger> findAllByTicket(Ticket ticket) {
        return passengerRepository.findAllByTicket(ticket);
    }

    @Override
    public Optional<Passenger> findByTicketIdAndPassengerId(long ticketId, long passengerId) {
        return passengerRepository.findByTicketIdAndPassengerId(ticketId, passengerId);
    }

    @Override
    public Passenger findMyPassengerByMemberId(Long memberId) {
        return passengerRepository.findMyPassengerByMemberId(memberId);
    }
}

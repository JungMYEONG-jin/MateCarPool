package com.example.eunboard.passenger.application.port.out;

import com.example.eunboard.member.domain.Member;
import com.example.eunboard.passenger.domain.Passenger;
import com.example.eunboard.ticket.domain.Ticket;

import java.util.List;

public interface PassengerRepositoryPort {
    Passenger save(Passenger passenger);
    boolean findRide(Passenger entity);
    Passenger findMyPassenger(Passenger entity);
    List<Passenger> getBoardingList(Long memberId);

    boolean existBoardingStatePassenger(Long memberId);

    Passenger findCurrentPassengerByMember(Member member);

    List<Passenger> findAllByTicket(Ticket ticket);
}

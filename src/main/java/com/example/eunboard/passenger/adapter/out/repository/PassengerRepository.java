package com.example.eunboard.passenger.adapter.out.repository;

import com.example.eunboard.passenger.domain.Passenger;
import com.example.eunboard.ticket.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PassengerRepository extends JpaRepository<Passenger, Long>, CustomPassengerRepository {
    List<Passenger> findAllByTicket(Ticket ticket);
}
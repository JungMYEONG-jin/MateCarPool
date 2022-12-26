package com.example.eunboard.passenger.application.port.out;

import com.example.eunboard.passenger.domain.Passenger;

import java.util.List;

public interface PassengerRepositoryPort {
    Passenger save(Passenger passenger);
    boolean findRide(Passenger entity);
    Passenger findMyPassenger(Passenger entity);
    List<Passenger> getBoardingList(Long memberId);
}

package com.example.eunboard.passenger.adapter.out.repository;

import com.example.eunboard.passenger.domain.Passenger;

public interface CustomPassengerRepository {
     boolean findRide(Passenger entity);
     Passenger findMyPassenger(Passenger entity);
}

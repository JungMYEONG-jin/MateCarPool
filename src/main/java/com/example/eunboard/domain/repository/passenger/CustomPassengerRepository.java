package com.example.eunboard.domain.repository.passenger;

import com.example.eunboard.domain.entity.Passenger;

public interface CustomPassengerRepository {
     boolean findRide(Passenger entity);
     Passenger findMyPassenger(Passenger entity);
}

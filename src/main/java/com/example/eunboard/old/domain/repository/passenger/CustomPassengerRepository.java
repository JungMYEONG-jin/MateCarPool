package com.example.eunboard.old.domain.repository.passenger;

import com.example.eunboard.old.domain.entity.Passenger;

public interface CustomPassengerRepository {
     boolean findRide(Passenger entity);
     Passenger findMyPassenger(Passenger entity);
}

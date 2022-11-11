package com.example.eunboard.domain.repository.passenger;

import com.example.eunboard.domain.entity.Passenger;

public interface CustomPassengerRepository {
    public boolean findRide(Passenger entity);
    public Passenger findMyPassenger(Passenger entity);
}

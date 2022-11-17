package com.example.eunboard.passenger.adapter.out.repository;

import com.example.eunboard.passenger.application.port.out.PassengerRepositoryPort;
import com.example.eunboard.passenger.domain.Passenger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
}

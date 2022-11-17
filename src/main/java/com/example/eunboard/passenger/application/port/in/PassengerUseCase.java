package com.example.eunboard.passenger.application.port.in;

public interface PassengerUseCase {
    void save(PassengerRequestDTO requestDTO);
    void takeDown(PassengerRequestDTO requestDTO);
}

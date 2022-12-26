package com.example.eunboard.passenger.application.port.in;

public interface PassengerUseCase {
    void save(PassengerCreateRequestDTO requestDTO);
    void takeDown(PassengerDeleteRequestDTO requestDTO);
}

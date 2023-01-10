package com.example.eunboard.passenger.application.port.in;

public interface PassengerUseCase {
    void ride(PassengerCreateRequestDTO requestDTO);

    void takeDown(PassengerDeleteRequestDTO requestDTO);
}

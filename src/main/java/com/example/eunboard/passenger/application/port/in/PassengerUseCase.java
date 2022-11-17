package com.example.eunboard.passenger.application.port.in;

import com.example.eunboard.old.domain.dto.request.PassengerRequestDTO;

public interface PassengerUseCase {
    void save(PassengerRequestDTO requestDTO);
    void takeDown(PassengerRequestDTO requestDTO);
}

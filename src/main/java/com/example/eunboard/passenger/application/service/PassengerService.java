package com.example.eunboard.passenger.application.service;

import com.example.eunboard.passenger.application.port.in.PassengerRequestDTO;
import com.example.eunboard.passenger.application.port.in.PassengerUseCase;
import com.example.eunboard.passenger.application.port.out.PassengerRepositoryPort;
import com.example.eunboard.passenger.domain.Passenger;
import com.example.eunboard.shared.exception.ErrorCode;
import com.example.eunboard.shared.exception.custom.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class PassengerService implements PassengerUseCase {

  private final PassengerRepositoryPort passengerRepository;

  public void save(PassengerRequestDTO requestDTO) {
    if (passengerRepository.findRide(PassengerRequestDTO.toEntity(requestDTO))) {
      throw new CustomException(ErrorCode.TICKET_PASS_EXIST.getMessage(), ErrorCode.TICKET_PASS_EXIST);
    }
    passengerRepository.save(PassengerRequestDTO.toEntity(requestDTO));
  }

  public void takeDown(PassengerRequestDTO requestDTO) {
    Passenger entity = passengerRepository.findMyPassenger(PassengerRequestDTO.toEntity(requestDTO));
    if (entity == null) {
      ErrorCode err = ErrorCode.TICKET_PASS_NOT_FOUND;
      throw new CustomException(err.getMessage(), err);
    }
    entity.setIsCancel(1);
    passengerRepository.save(entity);
  }

}

package com.example.eunboard.service;

import org.springframework.stereotype.Service;

import com.example.eunboard.domain.dto.request.PassengerRequestDTO;
import com.example.eunboard.domain.entity.Passenger;
import com.example.eunboard.domain.repository.PassengerQueryRepository;
import com.example.eunboard.domain.repository.PassengerRepository;
import com.example.eunboard.exception.ErrorCode;
import com.example.eunboard.exception.custom.CustomException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class PassengerService {

  private final PassengerRepository passengerRepository;
  private final PassengerQueryRepository passengerQueryRepository;

  public void save(PassengerRequestDTO requestDTO) {
    if (passengerQueryRepository.findRide(PassengerRequestDTO.toEntity(requestDTO))) {
      ErrorCode err = ErrorCode.TICKET_PASS_EXIST;
      throw new CustomException(err.getMessage(), err);
    }
    passengerRepository.save(PassengerRequestDTO.toEntity(requestDTO));
  }

  public void takeDown(PassengerRequestDTO requestDTO) {
    Passenger entity = passengerQueryRepository.findMyPassenger(PassengerRequestDTO.toEntity(requestDTO));
    if (entity == null) {
      ErrorCode err = ErrorCode.TICKET_PASS_NOT_FOUND;
      throw new CustomException(err.getMessage(), err);
    }
    entity.setIsCancel(1);
    passengerRepository.save(entity);
  }

}

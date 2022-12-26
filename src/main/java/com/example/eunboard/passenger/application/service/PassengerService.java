package com.example.eunboard.passenger.application.service;

import com.example.eunboard.passenger.application.port.in.PassengerCreateRequestDTO;
import com.example.eunboard.passenger.application.port.in.PassengerDeleteRequestDTO;
import com.example.eunboard.passenger.application.port.in.PassengerUseCase;
import com.example.eunboard.passenger.application.port.out.PassengerRepositoryPort;
import com.example.eunboard.passenger.domain.Passenger;
import com.example.eunboard.shared.exception.ErrorCode;
import com.example.eunboard.shared.exception.custom.CustomException;
import com.example.eunboard.ticket.application.port.out.TicketRepositoryPort;
import com.example.eunboard.ticket.domain.Ticket;
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
  private final TicketRepositoryPort ticketRepositoryPort;

  /**
   * 탑승 가능한 적정 인원인지 체크 필요.
   * @param requestDTO
   */
  public void save(PassengerCreateRequestDTO requestDTO) {
    /**
     * 이미 카풀 신청한게 있는지 체크
     */
    if (passengerRepository.findRide(PassengerCreateRequestDTO.toEntity(requestDTO))) {
      throw new CustomException(ErrorCode.TICKET_PASS_EXIST.getMessage(), ErrorCode.TICKET_PASS_EXIST);
    }
    /**
     * 티켓에 자리가 남았는지 체크
     */
    Ticket ticket = ticketRepositoryPort.findById(requestDTO.getTicketId()).orElseThrow(() -> new CustomException(ErrorCode.TICKET_NOT_FOUND.getMessage(), ErrorCode.TICKET_NOT_FOUND));
    if (ticket.getPassengerList().size()==ticket.getRecruitPerson()){
      throw new CustomException(ErrorCode.TICKET_IS_FULL.getMessage(), ErrorCode.TICKET_IS_FULL);
    }
    passengerRepository.save(PassengerCreateRequestDTO.toEntity(requestDTO));
  }

  public void takeDown(PassengerDeleteRequestDTO requestDTO) {
    Passenger entity = passengerRepository.findMyPassenger(PassengerDeleteRequestDTO.toEntity(requestDTO));
    if (entity == null) {
      throw new CustomException(ErrorCode.TICKET_PASS_NOT_FOUND.getMessage(), ErrorCode.TICKET_PASS_NOT_FOUND);
    }
    entity.setIsCancel(1);
    passengerRepository.save(entity);
  }

}

package com.example.eunboard.passenger.adapter.in;

import com.example.eunboard.passenger.application.port.in.PassengerRequestDTO;
import com.example.eunboard.passenger.application.port.in.PassengerUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/ride")
@RestController
public class PassengerController {

  private final PassengerUseCase passengerService;
  @PostMapping("/new")
  public void ride(@AuthenticationPrincipal UserDetails userDetails, @RequestBody PassengerRequestDTO requestDTO) {
    long memberId = Long.parseLong(userDetails.getUsername());
    requestDTO.setMemberId(memberId);
    passengerService.save(requestDTO);
  }

  @PostMapping("/delete")
  public void delete(@AuthenticationPrincipal UserDetails userDetails, @RequestBody PassengerRequestDTO requestDTO) {
    long memberId = Long.parseLong(userDetails.getUsername());
    requestDTO.setMemberId(memberId);
    passengerService.takeDown(requestDTO);
  }
}

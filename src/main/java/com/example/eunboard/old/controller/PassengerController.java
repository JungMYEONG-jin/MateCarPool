package com.example.eunboard.old.controller;

import com.example.eunboard.old.service.PassengerService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.eunboard.old.domain.dto.request.PassengerRequestDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/ride")
@RestController
public class PassengerController {

  private final PassengerService passengerService;

  @PostMapping("/new")
  public void ride(@AuthenticationPrincipal Long memberId, @RequestBody PassengerRequestDTO requestDTO) {
    requestDTO.setMemberId(memberId);
    requestDTO.setIsCancel(0);
    passengerService.save(requestDTO);
  }

  @PostMapping("/delete")
  public void delete(@AuthenticationPrincipal Long memberId, @RequestBody PassengerRequestDTO requestDTO) {
    requestDTO.setMemberId(memberId);
    passengerService.takeDown(requestDTO);
  }
}

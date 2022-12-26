package com.example.eunboard.passenger.adapter.in;

import com.example.eunboard.passenger.application.port.in.PassengerCreateRequestDTO;
import com.example.eunboard.passenger.application.port.in.PassengerDeleteRequestDTO;
import com.example.eunboard.passenger.application.port.in.PassengerUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/ride")
@RestController
public class PassengerController {

  private final PassengerUseCase passengerService;
  @PostMapping("/new")
  public ResponseEntity ride(@AuthenticationPrincipal UserDetails userDetails, @RequestBody PassengerCreateRequestDTO requestDTO) {
    long memberId = Long.parseLong(userDetails.getUsername());
    requestDTO.setMemberId(memberId);
    passengerService.save(requestDTO);
    Map<String, Object> map = new LinkedHashMap<>();
    map.put("status", HttpStatus.OK.value());
    map.put("message", "탑승 신청이 완료되었습니다.");
    return ResponseEntity.ok(map);
  }

  @PostMapping("/delete")
  public void delete(@AuthenticationPrincipal UserDetails userDetails, @RequestBody PassengerDeleteRequestDTO requestDTO) {
    long memberId = Long.parseLong(userDetails.getUsername());
    requestDTO.setMemberId(memberId);
    passengerService.takeDown(requestDTO);
  }
}

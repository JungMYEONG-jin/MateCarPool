package com.example.eunboard.passenger.adapter.in;

import com.example.eunboard.passenger.application.port.in.PassengerCreateRequestDTO;
import com.example.eunboard.passenger.application.port.in.PassengerDeleteRequestDTO;
import com.example.eunboard.passenger.application.port.in.PassengerUseCase;
import com.example.eunboard.shared.common.CommonResponse;
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

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/ride")
@RestController
public class PassengerController {

  private final PassengerUseCase passengerService;
  @PostMapping("/new")
  public ResponseEntity<CommonResponse> ride(@AuthenticationPrincipal UserDetails userDetails, @RequestBody PassengerCreateRequestDTO requestDTO) {
    long memberId = Long.parseLong(userDetails.getUsername());
    requestDTO.setMemberId(memberId);
    passengerService.save(requestDTO);

    CommonResponse res = CommonResponse.builder()
            .status(HttpStatus.OK.value())
            .message("탑승 신청이 완료되었습니다.")
            .build();
    return ResponseEntity.ok(res);
  }

  @PostMapping("/delete")
  public ResponseEntity<String> delete(@AuthenticationPrincipal UserDetails userDetails, @RequestBody PassengerDeleteRequestDTO requestDTO) {
    long memberId = Long.parseLong(userDetails.getUsername());
    requestDTO.setMemberId(memberId);
    passengerService.takeDown(requestDTO);
    return ResponseEntity.ok("성공적으로 삭제가 되었습니다.");
  }
}

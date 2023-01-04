package com.example.eunboard.passenger.adapter.in;

import com.example.eunboard.passenger.application.port.in.PassengerCreateRequestDTO;
import com.example.eunboard.passenger.application.port.in.PassengerDeleteRequestDTO;
import com.example.eunboard.passenger.application.port.in.PassengerUseCase;
import com.example.eunboard.shared.common.CommonResponse;
import com.example.eunboard.shared.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/passenger")
@RestController
public class PassengerController {

    private final PassengerUseCase passengerService;

    @Parameter(name = "userDetails", hidden = true)
    @Operation(summary = "탑승자 생성", description = "특정 카풀에 탑승자를 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "탑승 성공", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "400", description = "자리가 만석이거나 이미 카풀에 탑승중인 경우", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "해당 카풀을 찾을 수 없는 경우", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
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

    @Parameter(name = "userDetails", hidden = true)
    @Operation(summary = "탑승자 삭제", description = "해당 카풀에서 특정 탑승자를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "티켓을 찾을 수 없는 경우", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("")
    public ResponseEntity<String> delete(@AuthenticationPrincipal UserDetails userDetails, @RequestBody PassengerDeleteRequestDTO requestDTO) {
        long memberId = Long.parseLong(userDetails.getUsername());
        requestDTO.setMemberId(memberId);
        passengerService.takeDown(requestDTO);
        return ResponseEntity.ok("성공적으로 삭제가 되었습니다.");
    }
}

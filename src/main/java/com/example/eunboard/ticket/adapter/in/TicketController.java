package com.example.eunboard.ticket.adapter.in;

import com.example.eunboard.member.application.port.in.MemberUseCase;
import com.example.eunboard.shared.exception.ErrorCode;
import com.example.eunboard.shared.exception.ErrorResponse;
import com.example.eunboard.shared.exception.custom.CustomException;
import com.example.eunboard.ticket.application.port.in.*;
import com.example.eunboard.ticket.domain.TicketStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "카풀", description = "카풀 조회/생성/업데이트")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/ticket")
@RestController
public class TicketController {

  private final TicketUseCase ticketService;
  private final MemberUseCase memberUseCase;

  /**
   * 카풀 생성
   * @param userDetails
   * @param requestDto
   * @return
   */
  @Parameter(name = "userDetails", hidden = true)
  @Operation(summary = "카풀생성", description = "카풀생성을 진행합니다.")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "카풀생성 성공"),
          @ApiResponse(responseCode = "409", description = "이미 카풀을 생성한 유저", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
          @ApiResponse(responseCode = "403", description = "드라이버가 아닌 유저", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
  @PostMapping("/new")
  public ResponseEntity<Object> ticketCreate(@AuthenticationPrincipal UserDetails userDetails, @RequestBody TicketCreateRequestDto requestDto){
    long memberId = Long.parseLong(userDetails.getUsername());
    if(!memberUseCase.checkRole(memberId))
      throw new CustomException(ErrorCode.MEMBER_NOT_AUTHORITY.getMessage(), ErrorCode.MEMBER_NOT_AUTHORITY);
    requestDto.setMemberId(memberId);
    ticketService.createCarPool(requestDto);
    Map<String, Object> map = new HashMap<>();
    map.put("status", 200);
    map.put("message", "성공적으로 카풀이 생성되었습니다.");
    return ResponseEntity.ok(map);
  }

  /**
   * 특정 카풀 상세보기
   * @param id
   * @return
   */
  @Operation(summary = "카풀 상세보기", description = "카풀 상세보기를 진행합니다.")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "카풀 상세보기 성공", content = @Content(schema = @Schema(implementation = TicketDetailResponseDto.class))),
          @ApiResponse(responseCode = "404", description = "존재하지 않는 카풀", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
  @GetMapping("/read/{id}")
  public TicketDetailResponseDto read(@PathVariable long id) {
    return ticketService.readTicket(id);
  }

  @Operation(summary = "카풀 리스트", description = "카풀 리스트를 보여줍니다.")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "리스트 조회 성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = TicketDetailResponseDto.class))))})
  @GetMapping("/list")
  public List<TicketShortResponseDto> findAll() {
    return ticketService.getCarPoolList();
  }

  /**
   * 단건만 기록을 보는것??
   * @param userDetails
   * @return
   */
  @Parameter(name = "userDetails", hidden = true)
  @Operation(summary = "유저 카풀 조회", description = "자신의 카풀을 조회합니다(단건)")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "유저 카풀 조회 성공", content = @Content(schema = @Schema(implementation = TicketDetailResponseDto.class))),
          @ApiResponse(responseCode = "404", description = "존재하지 않는 카풀", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
  @GetMapping("/promise")
  public MyTicketDetailResponseDto promise(@AuthenticationPrincipal UserDetails userDetails) {
      long memberId = Long.parseLong(userDetails.getUsername());
      if(!memberUseCase.checkRole(memberId))
        throw new CustomException(ErrorCode.MEMBER_NOT_AUTHORITY.getMessage(), ErrorCode.MEMBER_NOT_AUTHORITY);
      return ticketService.getPromise(memberId);
  }


  /**
   * 내가 카풀 했던 기록보는거면 여러건 조회가 돼야 되지 않을까?
   * @param userDetails
   * @return
   */
  @Parameter(name = "userDetails", hidden = true)
  @Operation(summary = "유저 카풀 리스트 조회", description = "자신의 카풀을 조회합니다(리스트)")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "유저 카풀 리스트 조회 성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = TicketDetailResponseDto.class)))),
          @ApiResponse(responseCode = "404", description = "존재하지 않는 카풀", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
  @GetMapping("/promises")
  public List<TicketDetailResponseDto> promises(@AuthenticationPrincipal UserDetails userDetails) {
    long memberId = Long.parseLong(userDetails.getUsername());
    if (!memberUseCase.checkRole(memberId))
      throw new CustomException(ErrorCode.MEMBER_NOT_AUTHORITY.getMessage(), ErrorCode.MEMBER_NOT_AUTHORITY);
    return ticketService.getPromises(memberId);
  }

  @Parameter(name = "userDetails", hidden = true)
  @Operation(summary = "카풀 상태 변경", description = "카풀 상태 변경을 진행합니다.")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "카풀 상태 업데이트 성공"),
          @ApiResponse(responseCode = "403", description = "권한이 없는 유저", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
          @ApiResponse(responseCode = "404", description = "존재하지 않는 카풀", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
  @GetMapping("/update/{id}")
  public void update(@AuthenticationPrincipal UserDetails userDetails, @PathVariable long id, TicketStatus status) {
    long memberId = Long.parseLong(userDetails.getUsername());
    if (!memberUseCase.checkRole(memberId))
      throw new CustomException(ErrorCode.MEMBER_NOT_AUTHORITY.getMessage(), ErrorCode.MEMBER_NOT_AUTHORITY);
    ticketService.ticketStatusUpdate(memberId, id, status);
  }
  
}

package com.example.eunboard.member.adapter.in;

import com.example.eunboard.member.application.port.in.MemberUpdateRequestDTO;
import com.example.eunboard.member.application.port.in.MemberUpdateResponseDTO;
import com.example.eunboard.member.application.port.in.MemberUseCase;
import com.example.eunboard.member.application.port.in.ProfileResponseDto;
import com.example.eunboard.shared.common.CommonResponse;
import com.example.eunboard.shared.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "유저", description = "유저 조회/수정")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/member")
@RestController
public class MemberController {

    private final MemberUseCase memberService;

    /**
     * US-11 프로필 업데이트
     *
     * @param userDetails
     */
    @Parameter(name = "userDetails", hidden = true)
    @Operation(summary = "수정", description = "유저의 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 유저", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "이미 등록된 휴대폰 번호", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping(value = "/update/profile")
    public ResponseEntity<CommonResponse> updateMember(@AuthenticationPrincipal UserDetails userDetails,
            @RequestBody(required = true)  MemberUpdateRequestDTO memberUpdateRequestDTO) {
        Long memberId = Long.parseLong(userDetails.getUsername());
        memberService.checkMember(memberId);
        memberService.updateMember(memberId, memberUpdateRequestDTO);
        CommonResponse res = new CommonResponse("유저 정보 수정이 완료되었습니다.",HttpStatus.OK.value());
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(res);
    }

    @Parameter(name = "userDetails", hidden = true)
    @Operation(summary = "수정", description = "유저의 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 유저", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @ResponseBody
    @PutMapping(value = "/update/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommonResponse> updateMemberProfileImage(@AuthenticationPrincipal UserDetails userDetails,
                                       @RequestPart(required = false, name = "image") MultipartFile multipartFile) {
        Long memberId = Long.parseLong(userDetails.getUsername());
        memberService.checkMember(memberId);
        memberService.updateMemberProfileImage(memberId, multipartFile);
        CommonResponse res = new CommonResponse("프로필 이미지 변경이 완료되었습니다.",HttpStatus.OK.value());
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(res);
    }

    @Parameter(name = "userDetails", hidden = true)
    @Operation(summary = "수정", description = "유저 수정 페이지에 유저 정보를 제공합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 정보 로드 성공", content = @Content(schema = @Schema(implementation = MemberUpdateResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 유저", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @ResponseBody
    @GetMapping("/update")
    public ResponseEntity<MemberUpdateResponseDTO> updateMemberView(@AuthenticationPrincipal UserDetails userDetails) {
        long memberId = Long.parseLong(userDetails.getUsername());
        memberService.checkMember(memberId);
        return ResponseEntity.ok(memberService.getUpdateView(memberId));
    }

    /**
     * US-10 프로필 화면
     *
     * @param userDetails
     * @return
     */
    @Parameter(name = "userDetails", hidden = true)
    @Operation(summary = "프로필 조회", description = "자신의 프로필 정보를 봅니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로필 정보 로드 성공", content = @Content(schema = @Schema(implementation = ProfileResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 유저", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))

    })
    @GetMapping("/me")
    public ResponseEntity<ProfileResponseDto> getMyInfo(@AuthenticationPrincipal UserDetails userDetails) {
        long memberId = Long.parseLong(userDetails.getUsername());
        memberService.checkMember(memberId);
        ProfileResponseDto profileResponseDto;
        // Driver
        if (memberService.checkRole(memberId)) {
            profileResponseDto = memberService.getMyInfoForDriver(memberId);
        } else {
            profileResponseDto = memberService.getMyInfoForPassenger(memberId);
        }
        return ResponseEntity.ok(profileResponseDto);
    }

    /**
     * US-2 학번, 휴대폰 조회 기능 추가 요청
     */
    @Operation(summary = "학번 조회", description = "해당 학번이 사용가능한지 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용 가능한 학번"),
            @ApiResponse(responseCode = "409", description = "등록된 학번.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/check/class/{studentNumber}")
    public ResponseEntity<CommonResponse> validateStudentNumber(@PathVariable(name = "studentNumber") String studentNumber) {
        memberService.checkStudentNumber(studentNumber);
        CommonResponse res = new CommonResponse("사용가능한 학번입니다.", HttpStatus.OK.value());
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(res);
    }

    @Operation(summary = "전화번호 조회", description = "해당 전화번호가 사용가능한지 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용 가능한 번호"),
            @ApiResponse(responseCode = "409", description = "등록된 번호.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })

    @GetMapping("/check/phone/{phoneNumber}")
    public ResponseEntity<CommonResponse> validatePhoneNumber(@PathVariable(name = "phoneNumber") String phoneNumber) {
        memberService.checkPhoneNumber(phoneNumber);
        CommonResponse res = new CommonResponse("사용가능한 전화번호입니다.", HttpStatus.OK.value());
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(res);
    }
}

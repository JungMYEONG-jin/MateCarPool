package com.example.eunboard.auth.adapter.in;

import com.example.eunboard.auth.application.port.in.TokenDto;
import com.example.eunboard.auth.application.port.in.TokenRefreshDto;
import com.example.eunboard.auth.application.port.in.TokenRequestDto;
import com.example.eunboard.auth.application.port.in.TokenUseCase;
import com.example.eunboard.member.application.port.in.LoginRequestDto;
import com.example.eunboard.member.application.port.in.MemberRequestDTO;
import com.example.eunboard.shared.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final TokenUseCase loginService;

    @Operation(summary = "회원가입", description = "회원가입을 진행합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "회원가입 실패(값 입력 오류)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "회원가입 실패(존재하는 회원)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @PostMapping(value = "/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> signup(@RequestPart(required = true, name="memberRequestDTO") @Valid MemberRequestDTO memberRequestDTO, @RequestPart(required = false, name = "image") MultipartFile multipartFile){
        loginService.signup(memberRequestDTO, multipartFile);
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("status", HttpStatus.OK.value());
        map.put("message", "성공적으로 가입이 완료되었습니다.");
        return ResponseEntity.ok(map);
    }

    @Operation(summary = "로그인", description = "로그인을 진행합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = TokenDto.class))),
            @ApiResponse(responseCode = "400", description = "로그인 실패(ID, Password 오류)", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "로그인 실패(존재하지 않는 유저)", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody @Valid LoginRequestDto loginRequestDto){
        return ResponseEntity.ok(loginService.login(loginRequestDto));
    }

    @Operation(summary = "토근재발급", description = "토큰을 재발급합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "발급 성공", content = @Content(schema = @Schema(implementation = TokenDto.class))),
            @ApiResponse(responseCode = "400", description = "발급 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenRefreshDto tokenRefreshDto){
        return ResponseEntity.ok(loginService.reissue(tokenRefreshDto));
    }

    /**
     * US-12 로그아웃
     * @param tokenRequestDto
     * @return
     */
    @Operation(summary = "로그아웃", description = "로그아웃을 진행합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 성공"),
            @ApiResponse(responseCode = "498", description = "유효하지 않은 토큰", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody TokenRequestDto tokenRequestDto){
        loginService.logout(tokenRequestDto);
        return ResponseEntity.ok(LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)));
    }

    @PutMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestBody TokenRequestDto tokenRequestDto){
        loginService.withdraw(tokenRequestDto);
        return ResponseEntity.ok(LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)));
    }

}

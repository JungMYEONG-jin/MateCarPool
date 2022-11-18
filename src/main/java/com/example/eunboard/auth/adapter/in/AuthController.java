package com.example.eunboard.auth.adapter.in;

import com.example.eunboard.member.application.port.in.LoginRequestDto;
import com.example.eunboard.auth.application.port.in.TokenDto;
import com.example.eunboard.auth.application.port.in.TokenRequestDto;
import com.example.eunboard.auth.application.port.in.TokenUseCase;
import com.example.eunboard.member.application.port.in.MemberRequestDTO;
import com.example.eunboard.member.application.port.in.MemberResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final TokenUseCase loginService;

    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@RequestBody @Valid MemberRequestDTO memberRequestDTO){
        loginService.signup(memberRequestDTO);
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("status", HttpStatus.OK.value());
        map.put("message", "성공적으로 가입이 완료되었습니다.");
        return ResponseEntity.ok(map);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody @Valid LoginRequestDto loginRequestDto){
        return ResponseEntity.ok(loginService.login(loginRequestDto));
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenRequestDto tokenRequestDto){
        return ResponseEntity.ok(loginService.reissue(tokenRequestDto));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody TokenRequestDto tokenRequestDto){
        return ResponseEntity.ok(loginService.logout(tokenRequestDto));
    }

}

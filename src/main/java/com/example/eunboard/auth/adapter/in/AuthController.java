package com.example.eunboard.auth.adapter.in;

import com.example.eunboard.auth.application.port.in.TokenDto;
import com.example.eunboard.auth.application.port.in.TokenRequestDto;
import com.example.eunboard.auth.application.port.in.TokenUseCase;
import com.example.eunboard.member.application.port.in.MemberRequestDTO;
import com.example.eunboard.member.application.port.in.MemberResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final TokenUseCase loginService;

    @PostMapping("/signup")
    public ResponseEntity<MemberResponseDTO> signup(@RequestBody @Valid MemberRequestDTO memberRequestDTO){
        MemberResponseDTO signup = loginService.signup(memberRequestDTO);
        return ResponseEntity.ok(signup);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody MemberRequestDTO memberRequestDTO){
        return ResponseEntity.ok(loginService.login(memberRequestDTO));
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenRequestDto tokenRequestDto){
        return ResponseEntity.ok(loginService.reissue(tokenRequestDto));
    }

}

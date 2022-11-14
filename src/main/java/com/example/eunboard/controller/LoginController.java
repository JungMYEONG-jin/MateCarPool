package com.example.eunboard.controller;

import com.example.eunboard.domain.dto.request.MemberRequestDTO;
import com.example.eunboard.domain.dto.response.MemberResponseDTO;
import com.example.eunboard.domain.dto.token.TokenDto;
import com.example.eunboard.domain.dto.token.TokenRequestDto;
import com.example.eunboard.service.MemberTimetableService;
import com.example.eunboard.service.security.LoginService;
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
public class LoginController {
    private final LoginService loginService;

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

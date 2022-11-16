package com.example.eunboard.auth.application.port.in;

import com.example.eunboard.member.application.port.in.LoginRequestDto;
import com.example.eunboard.member.application.port.in.MemberRequestDTO;
import com.example.eunboard.member.application.port.in.MemberResponseDTO;

/**
 * author by : jmj
 * token은 폰번호, 학번을 기반으로 생성된다.
 * 만약 추가적으로 다른 정보도 필요하다면 수정이 필요할듯
 */
public interface TokenUseCase {
    MemberResponseDTO signup(MemberRequestDTO memberRequestDTO);
    TokenDto login(LoginRequestDto memberRequestDTO);
    // refresh token
    TokenDto reissue(TokenRequestDto tokenRequestDto);
    String logout(TokenRequestDto tokenRequestDto);
}

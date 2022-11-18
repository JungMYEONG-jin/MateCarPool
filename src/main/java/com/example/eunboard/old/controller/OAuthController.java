package com.example.eunboard.old.controller;

import com.example.eunboard.member.application.port.in.MemberResponseDTO;
import com.example.eunboard.member.domain.Member;
import com.example.eunboard.old.service.KakaoAPI;
import com.example.eunboard.shared.security.TokenProvider;
import com.example.eunboard.member.application.service.MemberService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/kakaoLogin")
@RestController
public class OAuthController {

    @Autowired
    private KakaoAPI kakaoService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private TokenProvider tokenProvider;

    // TODO: 토큰 리프레시

    @ResponseBody
    @GetMapping
    public MemberResponseDTO kakaoLogin(@RequestParam String code) {
        final String accessToken = kakaoService.getAccessToken(code);
        final Member member = memberService.create(kakaoService.getUserInfo(accessToken));
        final String token = tokenProvider.create(member);

        return MemberResponseDTO.toKakaoDTO(member, token);
    }
}
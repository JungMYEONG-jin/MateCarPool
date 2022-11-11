package com.example.eunboard.controller;

import com.example.eunboard.domain.dto.response.MemberResponseDTO;
import com.example.eunboard.domain.entity.Member;
import com.example.eunboard.security.TokenProvider;
import com.example.eunboard.service.KakaoAPI;
import com.example.eunboard.service.MemberService;

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

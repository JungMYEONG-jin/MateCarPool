package com.example.eunboard.service.security;

import com.example.eunboard.domain.dto.request.MemberRequestDTO;
import com.example.eunboard.domain.dto.response.MemberResponseDTO;
import com.example.eunboard.domain.dto.token.TokenDto;
import com.example.eunboard.domain.dto.token.TokenRequestDto;
import com.example.eunboard.domain.entity.Member;
import com.example.eunboard.domain.entity.RefreshToken;
import com.example.eunboard.domain.repository.member.MemberRepository;
import com.example.eunboard.domain.repository.token.RefreshTokenRepository;
import com.example.eunboard.exception.ErrorCode;
import com.example.eunboard.exception.custom.CustomException;
import com.example.eunboard.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LoginService {
        private final AuthenticationManagerBuilder authenticationManagerBuilder;
        private final MemberRepository memberRepository;
        private final PasswordEncoder passwordEncoder;
        private final TokenProvider tokenProvider;
        private final RefreshTokenRepository refreshTokenRepository;

        // 회원 가입
        public MemberResponseDTO signup(MemberRequestDTO memberRequestDTO){
                // 번호로 구분하자
            if (memberRepository.existsByPhoneNumber(memberRequestDTO.getPhoneNumber())){
                    throw new CustomException(ErrorCode.PHONE_IS_EXIST.getMessage(), ErrorCode.PHONE_IS_EXIST);
            }
            Member member = MemberRequestDTO.toMember(memberRequestDTO, passwordEncoder);
            return MemberResponseDTO.toDTO(memberRepository.save(member), null);
        }

        public TokenDto login(MemberRequestDTO memberRequestDTO){
            // phone, password based token
            UsernamePasswordAuthenticationToken authenticationToken = memberRequestDTO.toAuthentication();
            // run CustomUserDetailsService.loadUserByUsername
            Authentication authenticate = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            // create token
            TokenDto tokenDto = tokenProvider.generateToken(authenticate);
            // save refresh token
            RefreshToken refreshToken = RefreshToken.builder().key(authenticate.getName())
                    .value(tokenDto.getRefreshToken())
                    .build();
            refreshTokenRepository.save(refreshToken);
            // return token
            return tokenDto;
        }

        // refresh token
        public TokenDto reissue(TokenRequestDto tokenRequestDto){
            // validate refresh token
            if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())){
                throw new CustomException(ErrorCode.REFRESH_TOKEN_INVALID.getMessage(), ErrorCode.REFRESH_TOKEN_INVALID);
            }
            // get memberId
            Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());
            RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName()).orElseThrow(() -> new RuntimeException("This User is not logged in"));

            //token check
            if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())){
                throw new RuntimeException("토큰 정보가 일치하지 않습니다.");
            }

            // generate new token
            TokenDto tokenDto = tokenProvider.generateToken(authentication);
            // update repo
            RefreshToken newToken = refreshToken.updateValue(tokenDto.getRefreshToken());
            refreshTokenRepository.save(newToken); // 이미 id 존재하므로 update 시킴

            return tokenDto;

        }




}

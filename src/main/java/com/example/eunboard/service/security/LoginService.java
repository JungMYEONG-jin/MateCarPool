package com.example.eunboard.service.security;

import com.example.eunboard.domain.dto.request.MemberRequestDTO;
import com.example.eunboard.domain.dto.request.MemberTimetableRequestDTO;
import com.example.eunboard.domain.dto.response.MemberResponseDTO;
import com.example.eunboard.domain.dto.token.TokenDto;
import com.example.eunboard.domain.dto.token.TokenRequestDto;
import com.example.eunboard.domain.entity.Member;
import com.example.eunboard.domain.entity.MemberTimetable;
import com.example.eunboard.domain.entity.RefreshToken;
import com.example.eunboard.domain.repository.member.MemberRepository;
import com.example.eunboard.domain.repository.member.MemberTimetableRepository;
import com.example.eunboard.domain.repository.token.RefreshTokenRepository;
import com.example.eunboard.exception.ErrorCode;
import com.example.eunboard.exception.custom.CustomException;
import com.example.eunboard.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class LoginService {
        private final AuthenticationManagerBuilder authenticationManagerBuilder;
        private final MemberRepository memberRepository;
        private final PasswordEncoder passwordEncoder;
        private final TokenProvider tokenProvider;
        private final RefreshTokenRepository refreshTokenRepository;
        private final MemberTimetableRepository memberTimetableRepository;

        // 회원 가입
        public MemberResponseDTO signup(MemberRequestDTO memberRequestDTO){
            // 번호로 구분하자
            if (memberRepository.existsByPhoneNumber(memberRequestDTO.getPhoneNumber())){
                    throw new CustomException(ErrorCode.PHONE_IS_EXIST.getMessage(), ErrorCode.PHONE_IS_EXIST);
            }
            // DTO validation 체크로 해당 부분 제거해도 될듯
//            boolean isDup = memberRepository.findByMemberName(memberRequestDTO.getMemberName()).stream().
//                    anyMatch(member -> member.getStudentNumber().equals(memberRequestDTO.getStudentNumber()));
//            if (isDup){
//                throw new CustomException(ErrorCode.MEMBER_IS_EXIST.getMessage(), ErrorCode.MEMBER_IS_EXIST);
//            }
            Member member = MemberRequestDTO.toMember(memberRequestDTO, passwordEncoder);
            Member savedMember = memberRepository.save(member);
            List<MemberTimetableRequestDTO> memberTimeTable = memberRequestDTO.getMemberTimeTable();
            // 시간표 미 설정 유저
            if (memberTimeTable==null)
                return MemberResponseDTO.toDTO(savedMember, null);
            // 시간표 설정 유저
            List<MemberTimetable> timetableEntities = new ArrayList<>();
            memberTimeTable.forEach(timeTable -> {
                timeTable.setMemberId(savedMember.getMemberId());
                timetableEntities.add(MemberTimetableRequestDTO.toEntity(timeTable));
            });
            // 시간표 저장
            memberTimetableRepository.saveAll(timetableEntities);
            savedMember.setMemberTimeTableList(timetableEntities);
            return MemberResponseDTO.toDTOWithTimeTable(savedMember, null);
        }

    /**
     * token은 폰번호, 학번을 기반으로 생성된다.
     * 만약 추가적으로 다른 정보도 필요하다면 수정이 필요할듯
     * @param memberRequestDTO
     * @return
     */
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

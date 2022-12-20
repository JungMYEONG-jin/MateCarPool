package com.example.eunboard.auth.application.service;

import com.example.eunboard.auth.application.port.in.TokenDto;
import com.example.eunboard.auth.application.port.in.TokenRequestDto;
import com.example.eunboard.auth.application.port.in.TokenUseCase;
import com.example.eunboard.member.application.port.in.LoginRequestDto;
import com.example.eunboard.member.application.port.in.MemberRequestDTO;
import com.example.eunboard.member.application.port.in.MemberResponseDTO;
import com.example.eunboard.member.application.port.out.MemberRepositoryPort;
import com.example.eunboard.member.domain.Member;
import com.example.eunboard.shared.exception.ErrorCode;
import com.example.eunboard.shared.exception.custom.CustomException;
import com.example.eunboard.shared.security.TokenProvider;
import com.example.eunboard.shared.util.FileUploadUtils;
import com.example.eunboard.shared.util.MD5Generator;
import com.example.eunboard.timetable.application.port.in.MemberTimetableRequestDTO;
import com.example.eunboard.timetable.application.port.out.MemberTimetableRepositoryPort;
import com.example.eunboard.timetable.domain.MemberTimetable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthService implements TokenUseCase {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepositoryPort memberRepository;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final MemberTimetableRepositoryPort memberTimetableRepository;
    private final RedisTemplate redisTemplate;

    private static String refreshTokenPrefix = "RT:";

    // 회원 가입
    @Override
    public MemberResponseDTO signup(MemberRequestDTO memberRequestDTO, MultipartFile multipartFile){
        // phone check
        if (memberRepository.existsByPhoneNumber(memberRequestDTO.getPhoneNumber())){
            throw new CustomException(ErrorCode.PHONE_IS_EXIST.getMessage(), ErrorCode.PHONE_IS_EXIST);
        }
        // studentNumber check
        if (memberRepository.existsByStudentNumber(memberRequestDTO.getStudentNumber())) {
            throw new CustomException(ErrorCode.MEMBER_IS_EXIST.getMessage(), ErrorCode.MEMBER_IS_EXIST);
        }

        Member member = MemberRequestDTO.toMember(memberRequestDTO, passwordEncoder);
        Member savedMember = memberRepository.save(member);

        //get ID
        Long memberId = savedMember.getMemberId();

        // 이미지 업로드
        if (multipartFile!=null){
            String filename = multipartFile.getOriginalFilename();
            String ext = filename.substring(filename.lastIndexOf(".") + 1); // 확장자
            String newFileName = new MD5Generator(filename).toString() + "." + ext;
            FileUploadUtils.saveFile("/image/profiles/"+memberId, newFileName, multipartFile);
            savedMember.setProfileImage("/" + memberId + "/" + newFileName);
        }

        List<MemberTimetableRequestDTO> memberTimeTable = memberRequestDTO.getMemberTimeTable();
        // 시간표 미 설정 유저
        if (memberTimeTable==null)
            return MemberResponseDTO.toDTO(savedMember, null);
        // 시간표 설정 유저
        List<MemberTimetable> timetableEntities = new ArrayList<>();
        memberTimeTable.forEach(timeTable -> {
            timeTable.setMemberId(memberId);
            timetableEntities.add(MemberTimetableRequestDTO.toEntity(timeTable));
        });

        // 시간표 저장
        memberTimetableRepository.saveAll(timetableEntities);
        savedMember.setMemberTimeTableList(timetableEntities);
        return MemberResponseDTO.toDTOWithTimeTable(savedMember, null);
    }

    /**
     * request : memberName, password, phoneNumber
     * token은 폰번호, 학번을 기반으로 생성된다.
     * 만약 추가적으로 다른 정보도 필요하다면 수정이 필요할듯
     * @param loginRequestDto
     * @return
     */
    @Override
    public TokenDto login(LoginRequestDto loginRequestDto){
        //find phoneNumber
        Member member = memberRepository.findByPhoneNumber(loginRequestDto.getPhoneNumber()).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND.getMessage(), ErrorCode.MEMBER_NOT_FOUND));
        if (!member.getMemberName().equals(loginRequestDto.getMemberName()) || !passwordEncoder.matches(loginRequestDto.getStudentNumber(), member.getPassword()))
        {
            throw new CustomException(ErrorCode.LOGIN_INFO_NOT_MATCHED.getMessage(), ErrorCode.LOGIN_INFO_NOT_MATCHED);
        }
        // phone, studentNumber based token
        log.info("2");
        UsernamePasswordAuthenticationToken authenticationToken = loginRequestDto.toAuthentication();
        // run CustomUserDetailsService.loadUserByUsername
        log.info("3");
        Authentication authenticate = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // create token
        log.info("4");
        TokenDto tokenDto = tokenProvider.generateToken(authenticate);
        // save refresh token to redis
        redisTemplate.opsForValue().set(refreshTokenPrefix+authenticate.getName(), tokenDto.getRefreshToken(), tokenDto.getRefreshTokenExpiresIn(), TimeUnit.MILLISECONDS);
        // return token
        return tokenDto;
    }

    // refresh token
    @Override
    public TokenDto reissue(TokenRequestDto tokenRequestDto){
        // validate refresh token
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())){
            throw new CustomException(ErrorCode.REFRESH_TOKEN_INVALID.getMessage(), ErrorCode.REFRESH_TOKEN_INVALID);
        }
        // get memberId
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());
        // DB version
//        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName()).orElseThrow(() -> new RuntimeException("This User is not logged in"));
        // redis
        String refreshToken = (String)redisTemplate.opsForValue().get(refreshTokenPrefix + authentication.getName());
        //token check
        if (!refreshToken.equals(tokenRequestDto.getRefreshToken())){
            throw new RuntimeException("토큰 정보가 일치하지 않습니다.");
        }

        // generate new token
        TokenDto tokenDto = tokenProvider.generateToken(authentication);
        // update repo
//        RefreshToken newToken = refreshToken.updateValue(tokenDto.getRefreshToken());
//        refreshTokenRepository.save(newToken); // 이미 id 존재하므로 update 시킴
        // update redis
        redisTemplate.opsForValue().set(refreshTokenPrefix+authentication.getName(), tokenDto.getRefreshToken(), tokenDto.getRefreshTokenExpiresIn(), TimeUnit.MILLISECONDS);
        return tokenDto;
    }

    @Override
    public String logout(TokenRequestDto tokenRequestDto) {
        // 1. Access Token 검증
        if (!tokenProvider.validateToken(tokenRequestDto.getAccessToken())) {
            throw new CustomException(ErrorCode.TOKEN_INVALID.getMessage(), ErrorCode.TOKEN_INVALID);
        }
        // 2. Get MemberId from Access Token
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());
        // 3. Check whether the user's token exists if exist then delete
        if (redisTemplate.opsForValue().get("RT:" + authentication.getName()) != null) {
            // Delete Refresh Token
            redisTemplate.delete("RT:" + authentication.getName());
        }
        // 4. 해당 Access Token 유효시간 가지고 와서 BlackList 로 저장하기
        Long expiration = tokenProvider.getExpiration(tokenRequestDto.getAccessToken());
        redisTemplate.opsForValue()
                .set(tokenRequestDto.getAccessToken(), "logout", expiration, TimeUnit.MILLISECONDS);
        return "로그아웃 되었습니다.";
    }

}

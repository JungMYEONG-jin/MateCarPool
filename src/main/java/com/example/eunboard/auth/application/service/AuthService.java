package com.example.eunboard.auth.application.service;

import com.example.eunboard.auth.application.port.in.TokenDto;
import com.example.eunboard.auth.application.port.in.TokenRefreshDto;
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
import java.util.Date;
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

    // ?????? ??????
    @Override
    public MemberResponseDTO signup(MemberRequestDTO memberRequestDTO, MultipartFile multipartFile){
        // phone check
        if (memberRepository.existsByPhoneNumber(memberRequestDTO.getPhoneNumber())){
            throw new CustomException(ErrorCode.PHONE_IS_EXIST.getMessage(), ErrorCode.PHONE_IS_EXIST);
        }
        // studentNumber check
        if (memberRepository.existsByStudentNumber(memberRequestDTO.getStudentNumber())) {
            throw new CustomException(ErrorCode.STUDENT_NUM_EXIST.getMessage(), ErrorCode.STUDENT_NUM_EXIST);
        }

        Member member = MemberRequestDTO.toMember(memberRequestDTO, passwordEncoder);
        Member savedMember = memberRepository.save(member);

        //get ID
        Long memberId = savedMember.getMemberId();

        // ????????? ?????????
        if (multipartFile!=null){
            String filename = multipartFile.getOriginalFilename();
            String ext = filename.substring(filename.lastIndexOf(".") + 1); // ?????????
            String newFileName = new MD5Generator(filename).toString() + "." + ext;
            FileUploadUtils.saveFile("/image/profiles/"+memberId, newFileName, multipartFile);
            savedMember.setProfileImage("/" + memberId + "/" + newFileName);
        }

        List<MemberTimetableRequestDTO> memberTimeTable = memberRequestDTO.getMemberTimeTable();
        // ????????? ??? ?????? ??????
        if (memberTimeTable==null)
            return MemberResponseDTO.toDTO(savedMember, null);
        // ????????? ?????? ??????
        List<MemberTimetable> timetableEntities = new ArrayList<>();
        memberTimeTable.forEach(timeTable -> {
            timeTable.setMemberId(memberId);
            timetableEntities.add(MemberTimetableRequestDTO.toEntity(timeTable));
        });

        // ????????? ??????
        memberTimetableRepository.saveAll(timetableEntities);
        savedMember.setMemberTimeTableList(timetableEntities);
        return MemberResponseDTO.toDTO(savedMember, null);
    }

    /**
     * request : memberName, password, phoneNumber
     * token??? ?????????, ????????? ???????????? ????????????.
     * ?????? ??????????????? ?????? ????????? ??????????????? ????????? ????????????
     * @param loginRequestDto
     * @return
     */
    @Override
    public TokenDto login(LoginRequestDto loginRequestDto){
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
    public TokenDto reissue(TokenRefreshDto tokenRefreshDto){
        // validate refresh token
        if (!tokenProvider.validateToken(tokenRefreshDto.getRefreshToken())){
            throw new CustomException(ErrorCode.REFRESH_TOKEN_INVALID.getMessage(), ErrorCode.REFRESH_TOKEN_INVALID);
        }
        // get memberId
        Authentication authentication = tokenProvider.getAuthentication(tokenRefreshDto.getAccessToken());
        // DB version
//        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName()).orElseThrow(() -> new RuntimeException("This User is not logged in"));
        // redis
        String refreshToken = (String)redisTemplate.opsForValue().get(refreshTokenPrefix + authentication.getName());
        //token check
        if (!refreshToken.equals(tokenRefreshDto.getRefreshToken())){
            throw new RuntimeException("?????? ????????? ???????????? ????????????.");
        }

        // generate new token
        TokenDto tokenDto = tokenProvider.generateToken(authentication);
        // update repo
//        RefreshToken newToken = refreshToken.updateValue(tokenDto.getRefreshToken());
//        refreshTokenRepository.save(newToken); // ?????? id ??????????????? update ??????
        // update redis
        redisTemplate.opsForValue().set(refreshTokenPrefix+authentication.getName(), tokenDto.getRefreshToken(), tokenDto.getRefreshTokenExpiresIn(), TimeUnit.MILLISECONDS);
        return tokenDto;
    }

    @Override
    public String logout(TokenRequestDto tokenRequestDto) {
        // 1. Access Token ??????
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
        // 4. ?????? Access Token ???????????? ????????? ?????? BlackList ??? ????????????
        Long expiration = tokenProvider.getExpiration(tokenRequestDto.getAccessToken());
        redisTemplate.opsForValue()
                .set(tokenRequestDto.getAccessToken(), "logout", expiration, TimeUnit.MILLISECONDS);
        return "???????????? ???????????????.";
    }

    @Override
    public void withdraw(TokenRequestDto tokenRequestDto) {
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());
        String id = authentication.getName();
        // ?????? ?????? ??????
        long memberId = Long.parseLong(id);
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND.getMessage(), ErrorCode.MEMBER_NOT_FOUND));
        member.setIsRemoved(1);
        member.setDeleteDate(new Date());
        memberRepository.save(member);
        // ?????? ?????? ??????
        logout(tokenRequestDto);
    }


}

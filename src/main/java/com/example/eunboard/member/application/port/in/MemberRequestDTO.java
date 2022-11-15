package com.example.eunboard.member.application.port.in;

import com.example.eunboard.old.domain.dto.request.MemberTimetableRequestDTO;
import com.example.eunboard.member.domain.Member;
import com.example.eunboard.member.domain.MemberRole;
import com.example.eunboard.shared.validation.stdnum.StudentNumUnique;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class MemberRequestDTO {

    @JsonIgnore
    private long memberId;

    /** 인증토큰 */
    @JsonIgnore
    private String token;

    /** 학번 */
    @NotBlank
    @StudentNumUnique
    private String studentNumber;

    /** 이메일 */
    private String email;

    /** 비밀번호 */
    private String password;

    /** 이름 */
    private String memberName;

    /** 학과 */
    private String department;

    /** 연락처 */
    private String phoneNumber;

    /** 권한 */
    private MemberRole auth;

    /** 프로필 이미지 */
    private String profileImage;

    private boolean isMember;

    private String area;

    /** 등교일 */
    private List<MemberTimetableRequestDTO> memberTimeTable;

    public static Member toKakaoEntity(MemberRequestDTO dto) {
        return Member.builder()
                .memberName(dto.memberName)
                .email(dto.email)
                .password(dto.password)
                .build();
    }

    public static Member toMember(MemberRequestDTO dto, PasswordEncoder passwordEncoder){
        return Member.builder()
                .memberTimeTableList(dto.memberTimeTable.stream().map(MemberTimetableRequestDTO::toEntity)
                        .collect(Collectors.toList()))
                .memberId(dto.memberId)
                .password(passwordEncoder.encode(dto.password))
                .studentNumber(dto.studentNumber)
                .email(dto.email)
                .memberName(dto.memberName)
                .department(dto.department)
                .phoneNumber(dto.phoneNumber)
                .auth(dto.auth)
                .profileImage(dto.profileImage)
                .area(dto.area)
                .build();
    }

    public UsernamePasswordAuthenticationToken toAuthentication(){
        return new UsernamePasswordAuthenticationToken(phoneNumber, password);
    }

}

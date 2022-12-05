package com.example.eunboard.member.application.port.in;

import com.example.eunboard.timetable.application.port.in.MemberTimetableRequestDTO;
import com.example.eunboard.member.domain.Member;
import com.example.eunboard.member.domain.MemberRole;
import com.example.eunboard.shared.validation.stdnum.StudentNumUnique;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
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
    @Pattern(regexp = "[a-zA-Z0-9]+", message = "학번은 숫자, 영어만 입력 가능합니다.")
    private String studentNumber;

    /** 이메일 */
    private String email;

    /** 비밀번호 */
    private String password;

    /** 이름 */
    @Pattern(regexp = "[가-힣]+", message = "이름은 한글만 입력 가능합니다.")
    @Size(max = 4, message = "이름은 최대 4자까지 가능합니다.")
    @NotBlank
    private String memberName;

    /** 학과 */
    @Pattern(regexp = "[가-힣]+", message = "학과는 한글만 입력 가능합니다.")
    @NotBlank
    private String department;

    /** 연락처 */
    private String phoneNumber;

    /** 권한 */
    private MemberRole auth;

    /** 프로필 이미지 */
    @JsonIgnore
    private String profileImage;
    @JsonIgnore
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
                .profileImage(StringUtils.hasText(dto.profileImage)?dto.profileImage:"/image/profiles/default.png")
                .area(dto.area)
                .build();
    }

}

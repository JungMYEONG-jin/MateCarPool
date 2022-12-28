package com.example.eunboard.member.application.port.in;

import com.example.eunboard.member.domain.MemberRole;
import com.example.eunboard.timetable.application.port.in.MemberTimetableRequestDTO;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class MemberUpdateRequestDTO {
    /** 휴대폰 */
    @NotBlank
    private String phoneNumber;
    /** 권한 */
    private MemberRole auth;
    /** 등교일 */
    private List<MemberTimetableRequestDTO> memberTimeTable;
}

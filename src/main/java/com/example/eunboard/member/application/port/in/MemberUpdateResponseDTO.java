package com.example.eunboard.member.application.port.in;

import com.example.eunboard.member.domain.Member;
import com.example.eunboard.member.domain.MemberRole;
import com.example.eunboard.timetable.application.port.in.MemberTimetableResponseDTO;
import com.example.eunboard.timetable.domain.MemberTimetable;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class MemberUpdateResponseDTO {
    /** 휴대폰 */
    private String phoneNumber;
    /** 권한 */
    private MemberRole auth;
    /** 등교일 */
    private List<MemberTimetableResponseDTO> memberTimeTable;

    public static MemberUpdateResponseDTO of(Member member){
        List<MemberTimetable> times = member.getMemberTimeTableList();
        return MemberUpdateResponseDTO.builder()
                .phoneNumber(member.getPhoneNumber())
                .auth(member.getAuth())
                .memberTimeTable(times !=null? times.stream().map(MemberTimetableResponseDTO::toDTO).collect(Collectors.toList()) : new ArrayList<>())
                .build();
    }
}

package com.example.eunboard.member.application.port.in;

import com.example.eunboard.timetable.application.port.in.MemberTimetableResponseDTO;
import com.example.eunboard.member.domain.Member;
import com.example.eunboard.member.domain.MemberRole;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
public class MemberResponseDTO {

    private String token;

    @JsonIgnore
    private Long memberId;

    private String email;

    private String studentNumber;

    private String memberName;

    private String department;

    private String phoneNumber;

    private MemberRole auth;

    private String profileImage;

    private boolean isMember;

    private String area;

    private List<MemberTimetableResponseDTO> memberTimeTable = new ArrayList<>();

    public static MemberResponseDTO toDTO(Member entity, String token) {
        return MemberResponseDTO.builder()
                .token(token)
                .memberId(entity.getMemberId())
                .email(entity.getEmail())
                .studentNumber(entity.getStudentNumber())
                .memberName(entity.getMemberName())
                .department(entity.getDepartment())
                .phoneNumber(entity.getPhoneNumber())
                .auth(entity.getAuth())
                .profileImage(entity.getProfileImage())
                .isMember(entity.isMember())
                .area(entity.getArea())
                .memberTimeTable(entity.getMemberTimeTableList()!=null?entity.getMemberTimeTableList().stream().map(MemberTimetableResponseDTO::toDTO)
                        .collect(Collectors.toList()):new ArrayList<>())
                .build();
    }

}

package com.example.eunboard.member.application.port.in;

import com.example.eunboard.member.domain.Member;
import com.example.eunboard.member.domain.MemberRole;
import com.example.eunboard.ticket.application.port.in.TicketShortResponseDto;
import com.example.eunboard.ticket.domain.Ticket;
import com.example.eunboard.timetable.application.port.in.MemberTimetableResponseDTO;
import lombok.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class ProfileResponseDto {
    private String profileImage;
    private String memberName;
    private String studentNumber;
    private String department;
    private String phoneNumber;
    private List<MemberTimetableResponseDTO> memberTimeTable = new ArrayList<>();
    private MemberRole memberRole;
    private List<TicketShortResponseDto> tickets = new ArrayList<>();

    public static ProfileResponseDto of(Member member, List<Ticket> tickets){
        return ProfileResponseDto.builder()
                .profileImage(member.getProfileImage())
                .memberName(member.getMemberName())
                .studentNumber(member.getStudentNumber())
                .department(member.getDepartment())
                .phoneNumber(member.getPhoneNumber())
                .memberTimeTable(Optional.ofNullable(member.getMemberTimeTableList()).orElseGet(Collections::emptyList).stream().map(MemberTimetableResponseDTO::toDTO).collect(Collectors.toList()))
                .memberRole(member.getAuth())
                .tickets(Optional.ofNullable(tickets).orElseGet(Collections::emptyList).stream().map(TicketShortResponseDto::toDTO).collect(Collectors.toList()))
                .build();
    }
}

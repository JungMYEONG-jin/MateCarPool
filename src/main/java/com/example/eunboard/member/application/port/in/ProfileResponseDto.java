package com.example.eunboard.member.application.port.in;

import com.example.eunboard.ticket.application.port.in.TicketShortResponseDto;
import com.example.eunboard.timetable.application.port.in.MemberTimetableResponseDTO;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class ProfileResponseDto {
    private String department;
    private String memberName;
    private List<MemberTimetableResponseDTO> memberTimeTable = new ArrayList<>();
    private List<TicketShortResponseDto> tickets = new ArrayList<>();
}

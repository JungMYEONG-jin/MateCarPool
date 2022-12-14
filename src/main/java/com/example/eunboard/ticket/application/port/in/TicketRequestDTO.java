package com.example.eunboard.ticket.application.port.in;

import com.example.eunboard.member.domain.Member;
import com.example.eunboard.ticket.domain.Ticket;
import com.example.eunboard.ticket.domain.TicketStatus;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class TicketRequestDTO {

    private Long id;

    private Long memberId; // 멤버 일련번호

    private String startArea;

    private TicketStatus status;

    private String endArea;

    private String startDtime;

    private String kakaoOpenChatTitle;

    private String openChatUrl;

    private Integer recruitPerson;

    public static Ticket toEntity(TicketRequestDTO dto) {
        return Ticket.builder()
                .member(Member.builder().memberId(dto.memberId).build())
                .startArea(dto.startArea)
                .status(dto.status)
                .endArea(dto.endArea)
                .startDtime(dto.startDtime)
                .kakaoOpenChatTitle(dto.kakaoOpenChatTitle)
                .openChatUrl(dto.openChatUrl)
                .recruitPerson(dto.recruitPerson)
                .build();
    }

}

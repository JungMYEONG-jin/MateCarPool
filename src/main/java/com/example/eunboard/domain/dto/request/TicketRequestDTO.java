package com.example.eunboard.domain.dto.request;

import com.example.eunboard.domain.entity.Member;
import com.example.eunboard.domain.entity.Ticket;
import com.example.eunboard.domain.entity.TicketStatus;
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

    private String kakaoOpenChatUrl;

    private Integer recruitPerson;

    public static Ticket toEntity(TicketRequestDTO dto) {
        return Ticket.builder()
                .member(Member.builder().memberId(dto.memberId).build())
                .startArea(dto.startArea)
                .status(dto.status)
                .endArea(dto.endArea)
                .startDtime(dto.startDtime)
                .kakaoOpenChatTitle(dto.kakaoOpenChatTitle)
                .kakaoOpenChatUrl(dto.kakaoOpenChatUrl)
                .recruitPerson(dto.recruitPerson)
                .build();
        // ModelMapper modelMapper = new ModelMapper();

        // return modelMapper.map(dto, Ticket.class);
    }

}

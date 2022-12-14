package com.example.eunboard.ticket.application.port.in;

import com.example.eunboard.member.domain.Member;
import com.example.eunboard.ticket.domain.DayStatus;
import com.example.eunboard.ticket.domain.Ticket;
import com.example.eunboard.ticket.domain.TicketStatus;
import com.example.eunboard.ticket.domain.TicketType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class TicketCreateRequestDto {
    //    멤버 번호
    @JsonIgnore
    private Long memberId;
    // 출발지
    private String startArea;
    // 도착지
    private String endArea;
    // 탑승상세
    private String boardingPlace;
    // 월일 MMdd
    @Pattern(regexp = "(0[1-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])")
    private String startDayMonth;
    // 오전오후
    private DayStatus dayStatus;
    // 출발시간 hhmm
    @Pattern(regexp = "([0-1][0-9]|2[0-3])[0-5][0-9]")
    private String startTime;
    // 오픈채팅 url
    private String openChatUrl;
    // 탑승자 수
    private Integer recruitPerson;
    // 무료 유료
    private TicketType ticketType;
    // 직접 가격 설정
    private Long ticketPrice;

    public static Ticket toEntity(TicketCreateRequestDto dto) {
        int year = LocalDateTime.now().getYear();
        String dayTime = String.valueOf(year) + dto.startDayMonth + dto.startTime;
        // yyyyMMddHHmm
        return Ticket.builder()
                .member(Member.builder().memberId(dto.memberId).build())
                .startArea(dto.getStartArea())
                .status(TicketStatus.BEFORE)
                .endArea(dto.getEndArea())
                .startDtime(dayTime)
                .dayStatus(dto.getDayStatus())
                .boardingPlace(dto.getBoardingPlace())
                .openChatUrl(dto.getOpenChatUrl())
                .ticketType(dto.getTicketType())
                .recruitPerson(dto.getRecruitPerson())
                .build();
    }

}

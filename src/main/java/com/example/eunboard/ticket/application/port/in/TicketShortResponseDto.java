package com.example.eunboard.ticket.application.port.in;

import com.example.eunboard.ticket.domain.DayStatus;
import com.example.eunboard.ticket.domain.Ticket;
import com.example.eunboard.ticket.domain.TicketStatus;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TicketShortResponseDto {
    private Long id;
    // 드라이버 이미지
    private String profileImage;
    // 출발지
    private String startArea;
    // 오전 오후
    private DayStatus dayStatus;
    // 출발 시간
    private String startTime;
    // 탑승 인원
    private Integer recruitPerson;
    // 현재 인원수
    private Integer currentPersonCount;
    // 현재 티켓의 상태
    private TicketStatus ticketStatus;

    public static TicketShortResponseDto toDTO(Ticket entity) {
        String startDayTime = entity.getStartDtime();
        // yyyy mmdd hhmm
        String minuteHour = startDayTime.substring(8);
        return TicketShortResponseDto.builder()
                .id(entity.getId())
                .startArea(entity.getStartArea())
                .profileImage(entity.getMember().getProfileImage())
                .dayStatus(entity.getDayStatus())
                .startTime(minuteHour)
                .recruitPerson(entity.getRecruitPerson())
                .currentPersonCount(entity.getPassengerList().size())
                .ticketStatus(entity.getStatus())
                .build();
    }
}

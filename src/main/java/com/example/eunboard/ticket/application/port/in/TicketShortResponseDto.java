package com.example.eunboard.ticket.application.port.in;

import com.example.eunboard.passenger.domain.Passenger;
import com.example.eunboard.ticket.domain.DayStatus;
import com.example.eunboard.ticket.domain.Ticket;
import com.example.eunboard.ticket.domain.TicketStatus;
import com.example.eunboard.ticket.domain.TicketType;
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
    // 현재 티켓의 유료/무료 여부
    private TicketType ticketType;

    public static TicketShortResponseDto toDTO(Ticket entity) {
        // yyyy mmdd hhmm
        return TicketShortResponseDto.builder()
                .id(entity.getId())
                .startArea(entity.getStartArea())
                .profileImage(entity.getMember().getProfileImage())
                .dayStatus(entity.getDayStatus())
                .startTime(entity.getStartDtime())
                .recruitPerson(entity.getRecruitPerson())
                .currentPersonCount(
                        (int) entity.getPassengerList().stream()
                                .filter(passenger -> !passenger.isCancel())
                                .count()) // 취소 되지 않은 내용만 개수를 세야한다.
                .ticketStatus(entity.getStatus())
                .ticketType(entity.getTicketType())
                .build();
    }
}

package com.example.eunboard.passenger.application.port.in;

import com.example.eunboard.member.domain.Member;
import com.example.eunboard.passenger.domain.Passenger;
import com.example.eunboard.ticket.domain.Ticket;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PassengerCreateRequestDTO {
    private Long ticketId;
    @JsonIgnore
    private Long memberId;

    public static Passenger toEntity(PassengerCreateRequestDTO dto) {
        return Passenger.builder()
                .member(Member.builder().memberId(dto.memberId).build())
                .ticket(Ticket.builder().id(dto.ticketId).build())
                .isCancel(0)
                .build();
    }
}

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
public class PassengerDeleteRequestDTO {
    private Long passengerId;
    private Long ticketId;
    @JsonIgnore
    private Long memberId;
    private Integer isCancel;

    public static Passenger toEntity(PassengerDeleteRequestDTO dto) {
        return Passenger.builder()
                .id(dto.passengerId)
                .member(Member.builder().memberId(dto.memberId).build())
                .ticket(Ticket.builder().id(dto.ticketId).build())
                .isCancel(dto.isCancel)
                .build();

    }
}

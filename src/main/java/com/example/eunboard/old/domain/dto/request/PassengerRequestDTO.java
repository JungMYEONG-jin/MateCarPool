package com.example.eunboard.old.domain.dto.request;

import com.example.eunboard.member.domain.Member;
import com.example.eunboard.old.domain.entity.Passenger;
import com.example.eunboard.old.domain.entity.Ticket;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PassengerRequestDTO {

    private Long passengerId;

    private Long ticketId;

    @JsonIgnore
    private Long memberId;

    private Integer isCancel;

    public static Passenger toEntity(PassengerRequestDTO dto) {
        return Passenger.builder()
                .id(dto.passengerId)
                .member(Member.builder().memberId(dto.memberId).build())
                .ticket(Ticket.builder().id(dto.ticketId).build())
                .isCancel(dto.isCancel)
                .build();

    }
}

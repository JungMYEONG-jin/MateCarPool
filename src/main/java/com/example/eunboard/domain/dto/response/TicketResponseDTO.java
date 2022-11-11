package com.example.eunboard.domain.dto.response;

import com.example.eunboard.domain.entity.Passenger;
import com.example.eunboard.domain.entity.Ticket;
import com.example.eunboard.domain.entity.TicketStatus;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class TicketResponseDTO {

    private Long id;

    private String profileImage;

    private String startDtime;

    private String kakaoOpenChatUrl;

    private String kakaoOpenChatTitle;

    private String ticketPrice;

    private TicketStatus status;

    private Integer recruitPerson;

    private String startArea;

    private String endArea;

    private String memberName;

//    private List<Passenger> passengerList = new ArrayList<>();
    private Integer passengerCount;

    public static TicketResponseDTO toDTO(Ticket entity) {
        return TicketResponseDTO.builder()
                .id(entity.getId())
                .profileImage(entity.getMember().getProfileImage())
                .startDtime(entity.getStartDtime())
                .kakaoOpenChatTitle(entity.getKakaoOpenChatTitle())
                .kakaoOpenChatUrl(entity.getKakaoOpenChatUrl())
                .ticketPrice(entity.getTicketPrice())
                .status(entity.getStatus())
                .recruitPerson(entity.getRecruitPerson())
                .startArea(entity.getStartArea())
                .endArea(entity.getEndArea())
                .passengerCount(entity.getPassengerList().size())
                .memberName(entity.getMember().getMemberName())
                .build();
    }
}

package com.example.eunboard.ticket.application.port.in;

import com.example.eunboard.ticket.domain.Ticket;
import com.example.eunboard.ticket.domain.TicketStatus;
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

    private String openChatUrl;

    private String kakaoOpenChatTitle;

    private Long ticketPrice;

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
                .openChatUrl(entity.getOpenChatUrl())
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

package com.example.eunboard.ticket.application.port.in;

import com.example.eunboard.member.application.port.in.MemberResponseDTO;
import com.example.eunboard.passenger.domain.Passenger;
import com.example.eunboard.ticket.domain.DayStatus;
import com.example.eunboard.ticket.domain.Ticket;
import com.example.eunboard.ticket.domain.TicketType;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TicketDetailResponseDto {
    private Long id;
    /** 드라이버 이름 */
    private String memberName;
    // 드라이버 이미지
    private String profileImage;
    // 출발지
    private String startArea;
    // 도착지
    private String endArea;
    // 월일
    private String startDayMonth;
    // 오전 오후
    private DayStatus dayStatus;
    // 출발 시간
    private String startTime;
    // 탑승 장소 상세
    private String boardingPlace;
    // 탑승 인원
    private Integer recruitPerson;
    // 유료 무료
    private TicketType ticketType;
    // 추후 사용?
    private Long ticketPrice;
    // 탑승자 명단
    private List<MemberResponseDTO> passengers;

    public static TicketDetailResponseDto toDTO(Ticket entity) {
        String startDayTime = entity.getStartDtime();
        // yyyy mmdd hhmm
        String dayMonth = startDayTime.substring(4, 8);
        String minuteHour = startDayTime.substring(8);
        return TicketDetailResponseDto.builder()
                .id(entity.getId()) // 예약 하기 하려면 있어야 될듯?
                .startArea(entity.getStartArea())
                .endArea(entity.getEndArea())
                .profileImage(entity.getMember().getProfileImage())
                .memberName(entity.getMember().getMemberName())
                .dayStatus(entity.getDayStatus())
                .startTime(minuteHour)
                .startDayMonth(dayMonth)
                .ticketType(entity.getTicketType())
                .recruitPerson(entity.getRecruitPerson())
                .boardingPlace(entity.getBoardingPlace())
                // 탑승자에 대한 정보를 변환해서 반환해야함.
                .passengers(entity.getPassengerList().stream()
                        .map(Passenger::getMember)
                        .map((member -> MemberResponseDTO.toDTO(member, null)))
                        .collect(Collectors.toList()))
                .build();
    }
}

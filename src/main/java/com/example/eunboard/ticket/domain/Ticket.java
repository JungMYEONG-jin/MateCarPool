package com.example.eunboard.ticket.domain;

import com.example.eunboard.member.domain.Member;
import com.example.eunboard.old.domain.entity.BaseEntity;
import com.example.eunboard.passenger.domain.Passenger;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 이름만 ticket이지 카풀 리스트인듯?
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(name = "ticket")
@Entity
public class Ticket extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Long id;

    /** 드라이버 정보 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "member_id")
    @ToString.Exclude
    private Member member;

    /** 출발일 및 시간 */
    @Column(name = "start_dtime")
    private String startDtime;

    /** 오픈채팅방 링크 */
    private String openChatUrl;

    /** 오픈채팅방 이름 */
    @Column(name = "kakao_open_chat_title")
    private String kakaoOpenChatTitle;

    /** 티켓 상태*/
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 8)
    private TicketStatus status;

    /** 탑승 인원 */
    @Column(name = "recruit_person", length = 2)
    private Integer recruitPerson;

    /** 출발지 명*/
    @Column(name = "start_area", length = 50)
    private String startArea;

    /** 도착지 명*/
    @Column(name = "end_area", length = 50)
    private String endArea;

    /** 탑승자 연관관계 */
    @OneToMany(mappedBy = "ticket")
    @JsonIgnore
    @ToString.Exclude
    private List<Passenger> passengerList = new ArrayList<>();

    /** 탑승 상세 */
    private String boardingPlace;

    /** 오전 오후 */
    @Enumerated(EnumType.STRING)
    private DayStatus dayStatus;

    /** 무료/유료 */
    @Enumerated(EnumType.STRING)
    private TicketType ticketType;

    /** 가격 */
    private Long ticketPrice;

    public void updateStatus(TicketStatus status) {
        this.status = status;
    }

    public void delete() {
        status = TicketStatus.CANCEL;
    }
}

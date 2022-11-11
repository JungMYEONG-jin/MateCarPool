package com.example.eunboard.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    @Column(name = "kakao_open_chat_url")
    private String kakaoOpenChatUrl;

    /** 오픈채팅방 이름 */
    @Column(name = "kakao_open_chat_title")
    private String kakaoOpenChatTitle;

    /** 티켓 비용 (0=무료) */
    @ColumnDefault("'0'")
    @Column(name = "ticket_price", columnDefinition = "TINYINT(1)")
    private String ticketPrice;

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


    public void updateStatus(TicketStatus status) {
        this.status = status;
    }
}

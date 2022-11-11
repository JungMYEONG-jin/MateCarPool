package com.example.eunboard.domain.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@ToString
@Table(name = "passenger")
@Entity
public class Passenger extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "passenger_id")
    private Long id;

    /** 티켓 번호 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    @ToString.Exclude
    private Ticket ticket;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @ToString.Exclude
    private Member member;


    /** 취소 여부 */
    @Column(name = "is_cancel", columnDefinition = "TINYINT(1)")
    @ColumnDefault("'0'")
    private Integer isCancel;
}

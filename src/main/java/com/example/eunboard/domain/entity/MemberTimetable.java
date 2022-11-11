package com.example.eunboard.domain.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@ToString
@Table(name = "member_timetable")
@Entity
public class MemberTimetable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "member_timetable_id")
  private Long id;

  /** 멤버 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  @ToString.Exclude
  private Member member;

  @Column(name = "day_code", length = 1, nullable = false)
  private String dayCode;
}

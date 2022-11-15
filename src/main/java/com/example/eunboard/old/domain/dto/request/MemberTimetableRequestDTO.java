package com.example.eunboard.old.domain.dto.request;

import com.example.eunboard.member.domain.Member;
import com.example.eunboard.timetable.domain.MemberTimetable;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class MemberTimetableRequestDTO {

  @JsonIgnore
  private Long memberId;

  private String dayCode;

  @JsonIgnore
  private Long memberTimetableId;

  public static MemberTimetable toEntity(MemberTimetableRequestDTO dto) {
    return MemberTimetable.builder()
        .member(Member.builder().memberId(dto.memberId).build())
        .dayCode(dto.dayCode)
        .build();
  }

  public static MemberTimetable toDefault(MemberTimetableRequestDTO dto){
    return MemberTimetable.builder()
            .member(Member.builder().memberId(dto.memberId).build())
            .dayCode("1")
            .build();
  }
}

package com.example.eunboard.timetable.application.port.in;

import com.example.eunboard.timetable.domain.MemberTimetable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class MemberTimetableResponseDTO {

  @JsonIgnore
  private Long memberId;

  private String dayCode;

  @JsonIgnore
  private Long memberTimetableId;

  public static MemberTimetableResponseDTO toDTO(MemberTimetable entity) {
    return MemberTimetableResponseDTO.builder()
        .dayCode(entity.getDayCode())
        .build();
  }

}

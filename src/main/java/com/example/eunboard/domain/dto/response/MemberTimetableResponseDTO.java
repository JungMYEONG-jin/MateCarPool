package com.example.eunboard.domain.dto.response;

import com.example.eunboard.domain.entity.MemberTimetable;
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

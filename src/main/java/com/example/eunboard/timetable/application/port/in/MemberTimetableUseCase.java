package com.example.eunboard.timetable.application.port.in;

import com.example.eunboard.domain.dto.request.MemberTimetableRequestDTO;

import java.util.List;

public interface MemberTimetableUseCase {
    void saveAll(final Long memberId, List<MemberTimetableRequestDTO> timetableList);
}

package com.example.eunboard.timetable.application.port.in;

import java.util.List;

public interface MemberTimetableUseCase {
    void saveAll(final Long memberId, List<MemberTimetableRequestDTO> timetableList);
}

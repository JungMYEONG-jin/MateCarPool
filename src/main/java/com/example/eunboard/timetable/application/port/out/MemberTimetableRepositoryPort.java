package com.example.eunboard.timetable.application.port.out;

import com.example.eunboard.member.domain.Member;
import com.example.eunboard.timetable.domain.MemberTimetable;

import java.util.List;

public interface MemberTimetableRepositoryPort {
    List<MemberTimetable> findByMember(Member member);
    List<MemberTimetable> saveAll(List<MemberTimetable> memberTimetables);
    void deleteAllInBatch(List<MemberTimetable> memberTimetables);
}

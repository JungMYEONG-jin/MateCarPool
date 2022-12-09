package com.example.eunboard.timetable.adapter.out.repository;

import com.example.eunboard.member.domain.Member;
import com.example.eunboard.timetable.domain.MemberTimetable;
import com.example.eunboard.timetable.application.port.out.MemberTimetableRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MemberTimetableRepositoryAdapter implements MemberTimetableRepositoryPort {
    private final MemberTimetableRepository memberTimetableRepository;

    @Override
    public List<MemberTimetable> findByMember(Member member) {
        return memberTimetableRepository.findByMember(member);
    }

    @Override
    public List<MemberTimetable> saveAll(List<MemberTimetable> memberTimetables) {
        return memberTimetableRepository.saveAll(memberTimetables);
    }

    @Override
    public void deleteAllInBatch(List<MemberTimetable> memberTimetables) {
        memberTimetableRepository.deleteAllInBatch(memberTimetables);
    }
}

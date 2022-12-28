package com.example.eunboard.timetable.application.service;

import com.example.eunboard.member.domain.Member;
import com.example.eunboard.timetable.application.port.in.MemberTimetableRequestDTO;
import com.example.eunboard.timetable.application.port.in.MemberTimetableUseCase;
import com.example.eunboard.timetable.application.port.out.MemberTimetableRepositoryPort;
import com.example.eunboard.timetable.domain.MemberTimetable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberTimetableService implements MemberTimetableUseCase {

    private final MemberTimetableRepositoryPort memberTimetableRepository;

    @Override
    public void saveAll(final Long memberId, List<MemberTimetableRequestDTO> timetableList) {
        memberTimetableRepository
                .deleteAllInBatch(memberTimetableRepository.findByMember(Member.builder().memberId(memberId).build()));

        List<MemberTimetable> timetableEntities = new ArrayList<>();
        timetableList.forEach(timeTable -> {
            timeTable.setMemberId(memberId);
            timetableEntities.add(MemberTimetableRequestDTO.toEntity(timeTable));
        });

        memberTimetableRepository.saveAll(timetableEntities);
    }
}

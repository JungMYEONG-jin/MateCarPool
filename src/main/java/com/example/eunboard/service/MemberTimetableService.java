package com.example.eunboard.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.eunboard.domain.dto.request.MemberTimetableRequestDTO;
import com.example.eunboard.domain.entity.Member;
import com.example.eunboard.domain.entity.MemberTimetable;
import com.example.eunboard.domain.repository.MemberTimetableRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberTimetableService {

    private final MemberTimetableRepository memberTimetableRepository;

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

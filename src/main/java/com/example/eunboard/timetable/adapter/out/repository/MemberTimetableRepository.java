package com.example.eunboard.timetable.adapter.out.repository;

import com.example.eunboard.member.domain.Member;
import com.example.eunboard.timetable.domain.MemberTimetable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberTimetableRepository extends JpaRepository<MemberTimetable, Long> {

  List<MemberTimetable> findByMember(Member member);

}

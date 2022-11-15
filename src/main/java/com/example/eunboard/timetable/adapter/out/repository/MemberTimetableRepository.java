package com.example.eunboard.timetable.adapter.out.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.eunboard.member.domain.Member;
import com.example.eunboard.timetable.domain.MemberTimetable;

public interface MemberTimetableRepository extends JpaRepository<MemberTimetable, Long> {

  List<MemberTimetable> findByMember(Member member);

}

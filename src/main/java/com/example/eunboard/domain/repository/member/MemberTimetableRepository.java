package com.example.eunboard.domain.repository.member;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.eunboard.member.domain.Member;
import com.example.eunboard.member.domain.MemberTimetable;

public interface MemberTimetableRepository extends JpaRepository<MemberTimetable, Long> {

  List<MemberTimetable> findByMember(Member member);

}

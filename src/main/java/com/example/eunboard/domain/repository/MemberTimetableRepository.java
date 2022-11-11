package com.example.eunboard.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.eunboard.domain.entity.Member;
import com.example.eunboard.domain.entity.MemberTimetable;

public interface MemberTimetableRepository extends JpaRepository<MemberTimetable, Long> {

  List<MemberTimetable> findByMember(Member member);

}

package com.example.eunboard.member.adapter.out.repository;

import com.example.eunboard.member.domain.Member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByMemberId(Long memberId);
    Member findByEmail(String email);
    Boolean existsByEmail(String email);
    // 회원 검증 id가 이름인데 중복이 가능
    List<Member> findByMemberName(String memberName);
    Optional<Member> findByPhoneNumber(String phoneNumber);
    Optional<Member> findByStudentNumber(String studentNumber);
    // id가 중복이라 phone으로 구분해야할듯
    boolean existsByPhoneNumber(String phoneNumber);
    // 학번 중복 체크
    Boolean existsByStudentNumber(String studentNumber);

}
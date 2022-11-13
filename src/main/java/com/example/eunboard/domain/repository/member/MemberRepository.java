package com.example.eunboard.domain.repository.member;

import com.example.eunboard.domain.entity.Member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByMemberId(Long memberId);
    Member findByEmail(String email);
    Boolean existsByEmail(String email);
    // 회원 검증 id가 이름인데 중복이 가능
    List<Member> findByMemberName(String memberName);
}
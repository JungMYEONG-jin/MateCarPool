package com.example.eunboard.domain.repository;

import com.example.eunboard.domain.entity.Member;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByMemberId(Long memberId);

    Member findByEmail(String email);

    Boolean existsByEmail(String email);
}
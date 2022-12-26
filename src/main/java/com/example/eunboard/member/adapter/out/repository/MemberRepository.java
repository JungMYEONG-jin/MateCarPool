package com.example.eunboard.member.adapter.out.repository;

import com.example.eunboard.member.domain.Member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, CustomMemberRepository {
    Member findByMemberId(Long memberId);
}
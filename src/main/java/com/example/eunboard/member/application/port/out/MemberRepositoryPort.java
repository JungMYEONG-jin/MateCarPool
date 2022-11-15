package com.example.eunboard.member.application.port.out;

import com.example.eunboard.member.domain.Member;

import java.util.List;
import java.util.Optional;

/**
 * repository port
 * 즉 jpa method 이름 interface 한것
 */
public interface MemberRepositoryPort {
    Optional<Member> findById(Long id);
    Member save(Member member);
    boolean existsByEmail(String email);
    Member findByEmail(String email);
    Member findByMemberId(Long id);
    Optional<Member> findByStudentNumber(String studentnumber);
    boolean existsByPhoneNumber(String phoneNumber);
    List<Member> findByMemberName(String memberName);
}

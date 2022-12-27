package com.example.eunboard.member.application.port.out;

import com.example.eunboard.member.domain.Member;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

/**
 * repository port
 * 즉 jpa method 이름 interface 한것
 */
public interface MemberRepositoryPort {
    Optional<Member> findById(Long id);
    Member save(Member member);
    Member findByMemberId(Long id);
    Optional<Member> findByStudentNumber(String studentNumber);
    Optional<Member> findByPhoneNumber(String phoneNumber);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByStudentNumber(String studentNumber);
}

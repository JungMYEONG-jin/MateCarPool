package com.example.eunboard.member.adapter.out.repository;

import com.example.eunboard.member.application.port.out.MemberRepositoryPort;
import com.example.eunboard.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryAdapter implements MemberRepositoryPort {

    private final MemberRepository memberRepository;

    @Override
    public Optional<Member> findById(Long id) {
        return memberRepository.findById(id);
    }

    @Override
    public Member save(Member member) {
        return memberRepository.save(member);
    }

    @Override
    public Member findByMemberId(Long id) {
        return memberRepository.findByMemberId(id);
    }

    @Override
    public Optional<Member> findByStudentNumber(String studentNumber) {
        return Optional.ofNullable(memberRepository.findByStudentNumber(studentNumber));
    }

    @Override
    public Optional<Member> findByPhoneNumber(String phoneNumber) {
        return Optional.ofNullable(memberRepository.findByPhoneNumber(phoneNumber));
    }

    @Override
    public boolean existsByPhoneNumber(String phoneNumber) {
        return memberRepository.checkPhoneNumber(phoneNumber);
    }

    @Override
    public boolean existsByStudentNumber(String studentNumber) {
        return memberRepository.checkStudentNumber(studentNumber);
    }
}

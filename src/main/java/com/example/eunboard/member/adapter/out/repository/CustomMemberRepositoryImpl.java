package com.example.eunboard.member.adapter.out.repository;

import com.example.eunboard.member.domain.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import static com.example.eunboard.member.domain.QMember.member;

@Transactional
@RequiredArgsConstructor
public class CustomMemberRepositoryImpl implements CustomMemberRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public boolean checkPhoneNumber(String phoneNumber) {
        return jpaQueryFactory.selectFrom(member)
                .where(member.phoneNumber.eq(phoneNumber),
                        member.isRemoved.eq(0))
                .fetchOne() != null;
    }

    @Override
    public boolean checkStudentNumber(String studentNumber) {
        return jpaQueryFactory.selectFrom(member)
                .where(member.studentNumber.eq(studentNumber),
                        member.isRemoved.eq(0))
                .fetchOne() != null;
    }

    @Override
    public Member findByStudentNumber(String studentNumber) {
        return jpaQueryFactory.selectFrom(member)
                .where(member.studentNumber.eq(studentNumber),
                        member.isRemoved.eq(0))
                .fetchOne();
    }

    @Override
    public Member findByPhoneNumber(String phoneNumber) {
        return jpaQueryFactory.selectFrom(member)
                .where(member.phoneNumber.eq(phoneNumber),
                        member.isRemoved.eq(0))
                .fetchOne();
    }
}

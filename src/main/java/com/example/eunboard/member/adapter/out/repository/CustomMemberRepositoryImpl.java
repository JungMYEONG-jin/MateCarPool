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

    /**
     * 해당 휴대폰 번호로 탈퇴하지 않은 회원이 존재하는지 체크
     * @param phoneNumber
     * @return true : 존재, false : 미존재
     */
    @Override
    public boolean checkPhoneNumber(String phoneNumber) {
        return jpaQueryFactory.selectFrom(member)
                .where(member.phoneNumber.eq(phoneNumber),
                        member.isRemoved.eq(0))
                .fetchOne() != null;
    }

    /**
     * 해당 학번으로 탈퇴하지 않은 회원이 존재하는지 체크
     * @param studentNumber
     * @return true : 존재, false : 미존재
     */
    @Override
    public boolean checkStudentNumber(String studentNumber) {
        return jpaQueryFactory.selectFrom(member)
                .where(member.studentNumber.eq(studentNumber),
                        member.isRemoved.eq(0))
                .fetchOne() != null;
    }

    /**
     * 학번으로 유효한 사용자를 찾는다.
     * @param studentNumber
     * @return
     */
    @Override
    public Member findByStudentNumber(String studentNumber) {
        return jpaQueryFactory.selectFrom(member)
                .where(member.studentNumber.eq(studentNumber),
                        member.isRemoved.eq(0))
                .fetchOne();
    }

    /**
     * 휴대폰으로 유효한 사용자를 찾는다.
     * @param phoneNumber
     * @return
     */
    @Override
    public Member findByPhoneNumber(String phoneNumber) {
        return jpaQueryFactory.selectFrom(member)
                .where(member.phoneNumber.eq(phoneNumber),
                        member.isRemoved.eq(0))
                .fetchOne();
    }
}

package com.example.eunboard.member.adapter.out.repository;

import com.example.eunboard.member.domain.Member;

public interface CustomMemberRepository {
    // id가 중복이라 phone으로 구분해야할듯
    boolean checkPhoneNumber(String phoneNumber);
    // 학번 중복 체크
    boolean checkStudentNumber(String studentNumber);
    Member findByStudentNumber(String studentNumber);
    Member findByPhoneNumber(String phoneNumber);
}

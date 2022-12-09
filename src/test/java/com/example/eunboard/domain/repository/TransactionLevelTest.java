package com.example.eunboard.domain.repository;

import com.example.eunboard.member.application.port.out.MemberRepositoryPort;
import com.example.eunboard.member.domain.Member;
import com.example.eunboard.member.domain.MemberRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TransactionLevelTest {

    @Autowired
    MemberRepositoryPort memberRepositoryPort;

    @BeforeEach
    void init(){
        Member build = Member.builder().isMember(true)
                .auth(MemberRole.PASSENGER)
                .area("옥동")
                .department("건축")
                .email("aaa@abc.com")
                .memberName("mj")
                .password("114")
                .phoneNumber("212121")
                .studentNumber("114").build();
        memberRepositoryPort.save(build);
    }


    @Test
    void readCommittedTest() {

        Member member = memberRepositoryPort.findById(1L).orElse(null);
        Member member2 = memberRepositoryPort.findById(1L).orElse(null);
        System.out.println("member = " + member);
        System.out.println("member2 = " + member2);
    }
}

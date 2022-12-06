package com.example.eunboard.controller;

import com.example.eunboard.member.application.port.in.LoginRequestDto;
import com.example.eunboard.member.application.port.in.MemberRequestDTO;
import com.example.eunboard.member.domain.MemberRole;
import com.example.eunboard.ticket.application.port.in.TicketRequestDTO;
import com.example.eunboard.ticket.domain.TicketStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class LoginControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void authTest() throws Exception {
        this.mockMvc.perform(get("/auth/test").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Description("회원가입")
    @Test
    void signUpTest() throws Exception {
        MemberRequestDTO mj = MemberRequestDTO.builder().studentNumber("2015")
                .department("통계학과")
                .auth(MemberRole.PASSENGER)
                .memberName("명진")
                .phoneNumber("010")
                .memberTimeTable(new ArrayList<>())
                .build();

        ResultActions perform = this.mockMvc.perform(post("/auth/signup").
                contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(mj)));
        perform.andExpect(status().isOk()).
                andExpect(jsonPath("memberName").value("명진")).
                andExpect(jsonPath("phoneNumber").value("010"));
    }

    @Description("회원가입 이름 한글만")
    @Test
    void signUpNameValidationTest() throws Exception {
        MemberRequestDTO mj = MemberRequestDTO.builder().studentNumber("2015")
                .department("통계학과")
                .auth(MemberRole.PASSENGER)
                .memberName("JMJ")
                .phoneNumber("010")
                .memberTimeTable(new ArrayList<>())
                .build();

        ResultActions perform = this.mockMvc.perform(post("/auth/signup").
                contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(mj)));
        perform.andExpect(status().isBadRequest());
    }

    @Description("회원가입 학과 한글만")
    @Test
    void signUpDepartmentTest() throws Exception {
        MemberRequestDTO mj = MemberRequestDTO.builder().studentNumber("2015")
                .department("statistics")
                .auth(MemberRole.PASSENGER)
                .memberName("명진")
                .phoneNumber("010")
                .memberTimeTable(new ArrayList<>())
                .build();

        ResultActions perform = this.mockMvc.perform(post("/auth/signup").
                contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(mj)));
        perform.andExpect(status().isBadRequest());
    }

    @Description("회원가입 학번 영문+숫자")
    @Test
    void signUpStudentNumberTest() throws Exception {
        MemberRequestDTO mj = MemberRequestDTO.builder().studentNumber("SH2015")
                .department("statistics")
                .auth(MemberRole.PASSENGER)
                .memberName("명진")
                .phoneNumber("010")
                .memberTimeTable(new ArrayList<>())
                .build();

        ResultActions perform = this.mockMvc.perform(post("/auth/signup").
                contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(mj)));
        perform.andExpect(status().isBadRequest());
    }

    @Description("회원가입 후 로그인")
    @Test
    void signAndLoginTest() throws Exception {
        MemberRequestDTO mj = MemberRequestDTO.builder().studentNumber("2015")
                .department("통계학과")
                .auth(MemberRole.PASSENGER)
                .memberName("명진")
                .phoneNumber("010")
                .memberTimeTable(new ArrayList<>())
                .build();

        ResultActions perform = this.mockMvc.perform(post("/auth/signup").
                contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(mj)));
        perform.andDo(print());

        LoginRequestDto login = LoginRequestDto.builder().memberName("명진").phoneNumber("010").build();
        ResultActions perform2 = this.mockMvc.perform(post("/auth/login").
                contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(login)));
        perform2.andExpect(status().isOk());
        perform2.andDo(print());
    }

    @Description("티켓 발급 테스트")
    @Test
    void newTicketTest() throws Exception {
        MemberRequestDTO mj = MemberRequestDTO.builder().studentNumber("2015")
                .department("통계학과")
                .auth(MemberRole.PASSENGER)
                .memberName("명진")
                .phoneNumber("010")
                .memberTimeTable(new ArrayList<>())
                .build();

        ResultActions perform = this.mockMvc.perform(post("/auth/signup").
                contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(mj)));
        perform.andDo(print());

        LoginRequestDto login = LoginRequestDto.builder().memberName("명진").phoneNumber("010").build();
        ResultActions perform2 = this.mockMvc.perform(post("/auth/login").
                contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(login)));
        perform2.andExpect(status().isOk());
        perform2.andDo(print());

        TicketRequestDTO ticket = TicketRequestDTO.builder().memberId(1L).startArea("인동").endArea("대학교")
                .status(TicketStatus.BEFORE).build();
        ResultActions perform3 = this.mockMvc.perform(post("/ticket/new").contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(ticket)));
        perform3.andExpect(status().isOk());
        perform3.andDo(print());
    }

    @Description("이름 중복 허용 테스트")
    @Test
    void signDuplicateMemberNameTest() throws Exception {
        MemberRequestDTO mj = MemberRequestDTO.builder().studentNumber("2015")
                .department("통계학과")
                .auth(MemberRole.PASSENGER)
                .memberName("명진")
                .phoneNumber("010")
                .memberTimeTable(new ArrayList<>())
                .build();

        MemberRequestDTO test = MemberRequestDTO.builder().studentNumber("20151")
                .department("통계학과")
                .auth(MemberRole.PASSENGER)
                .memberName("명진")
                .phoneNumber("01044")
                .memberTimeTable(new ArrayList<>())
                .build();

        ResultActions perform = this.mockMvc.perform(post("/auth/signup").
                contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(mj)));

        ResultActions perform2 = this.mockMvc.perform(post("/auth/signup").
                contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(test)));

        perform.andExpect(status().isOk()).
                andExpect(jsonPath("memberName").value("명진")).
                andExpect(jsonPath("phoneNumber").value("010")).
                andExpect(jsonPath("studentNumber").value("2015"));
        perform.andDo(print());

        perform2.andExpect(status().isOk()).
                andExpect(jsonPath("memberName").value("명진")).
                andExpect(jsonPath("phoneNumber").value("01044")).
                andExpect(jsonPath("studentNumber").value("20151"));

        perform2.andDo(print());
    }


    @Description("학번 중복 실패 테스트")
    @Test
    void signDuplicateStudentNumberTest() throws Exception {
        MemberRequestDTO mj = MemberRequestDTO.builder().studentNumber("2015")
                .department("통계학과")
                .auth(MemberRole.PASSENGER)
                .memberName("명진")
                .phoneNumber("010")
                .memberTimeTable(new ArrayList<>())
                .build();

        MemberRequestDTO test = MemberRequestDTO.builder().studentNumber("2015")
                .department("통계학과")
                .auth(MemberRole.PASSENGER)
                .memberName("명진")
                .phoneNumber("01044")
                .memberTimeTable(new ArrayList<>())
                .build();

        ResultActions perform = this.mockMvc.perform(post("/auth/signup").
                contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(mj)));

        perform.andDo(print());


        ResultActions perform2 = this.mockMvc.perform(post("/auth/signup").
                contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(test)));

        perform2.andExpect(status().isBadRequest());
        perform2.andDo(print());
    }
}
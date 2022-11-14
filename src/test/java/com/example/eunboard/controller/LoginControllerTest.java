package com.example.eunboard.controller;

import com.example.eunboard.domain.dto.request.MemberRequestDTO;
import com.example.eunboard.domain.entity.MemberRole;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
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
        MemberRequestDTO mj = MemberRequestDTO.builder().studentNumber("2015").password("2015")
                .auth(MemberRole.PASSENGER)
                .memberName("mj")
                .phoneNumber("010")
                .memberTimeTable(new ArrayList<>())
                .build();

        ResultActions perform = this.mockMvc.perform(post("/auth/signup").
                contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(mj)));
        perform.andExpect(status().isOk()).
                andExpect(jsonPath("memberName").value("mj")).
                andExpect(jsonPath("phoneNumber").value("010"));
    }

    @Description("회원가입 후 로그인")
    @Test
    void signAndLoginTest() throws Exception {
        MemberRequestDTO mj = MemberRequestDTO.builder().studentNumber("2015").password("2015")
                .auth(MemberRole.PASSENGER)
                .memberName("mj")
                .phoneNumber("010")
                .memberTimeTable(new ArrayList<>())
                .build();

        ResultActions perform = this.mockMvc.perform(post("/auth/signup").
                contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(mj)));

        ResultActions perform2 = this.mockMvc.perform(post("/auth/login").
                contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(mj)));
        perform2.andExpect(status().isOk());
        String result = perform2.toString();
    }

    @Description("이름 중복 허용 테스트")
    @Test
    void signDuplicateMemberNameTest() throws Exception {
        MemberRequestDTO mj = MemberRequestDTO.builder().studentNumber("2015").password("2015")
                .auth(MemberRole.PASSENGER)
                .memberName("mj")
                .phoneNumber("010")
                .memberTimeTable(new ArrayList<>())
                .build();

        MemberRequestDTO test = MemberRequestDTO.builder().studentNumber("20151").password("20151")
                .auth(MemberRole.PASSENGER)
                .memberName("mj")
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
                andExpect(jsonPath("memberName").value("mj")).
                andExpect(jsonPath("phoneNumber").value("010")).
                andExpect(jsonPath("studentNumber").value("2015"));
        perform.andDo(print());

        perform2.andExpect(status().isOk()).
                andExpect(jsonPath("memberName").value("mj")).
                andExpect(jsonPath("phoneNumber").value("01044")).
                andExpect(jsonPath("studentNumber").value("20151"));

        perform2.andDo(print());
    }


    @Description("학번 중복 실패 테스트")
    @Test
    void signDuplicateStudentNumberTest() throws Exception {
        MemberRequestDTO mj = MemberRequestDTO.builder().studentNumber("2015").password("2015")
                .auth(MemberRole.PASSENGER)
                .memberName("mj")
                .phoneNumber("010")
                .memberTimeTable(new ArrayList<>())
                .build();

        MemberRequestDTO test = MemberRequestDTO.builder().studentNumber("2015").password("2015")
                .auth(MemberRole.PASSENGER)
                .memberName("mj")
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

        perform2.andDo(print());
    }
}
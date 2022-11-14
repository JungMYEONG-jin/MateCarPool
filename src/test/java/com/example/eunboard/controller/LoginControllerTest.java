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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
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

    @Test
    void signUpTest() throws Exception {
        MemberRequestDTO memberRequestDTO = new MemberRequestDTO();
        memberRequestDTO.setArea("대덕");
        memberRequestDTO.setMemberName("mj");
        memberRequestDTO.setAuth(MemberRole.PASSENGER);
        memberRequestDTO.setDepartment("software");
        memberRequestDTO.setEmail("abc@abc.com");
        memberRequestDTO.setPassword("2011");
        memberRequestDTO.setPhoneNumber("01045444544");
        memberRequestDTO.setMemberTimeTable(new ArrayList<>());

        ResultActions perform = this.mockMvc.perform(post("/auth/signup").
                contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(memberRequestDTO)));
        perform.andExpect(status().isOk()).
                andExpect(jsonPath("memberName").value("mj")).
                andExpect(jsonPath("phoneNumber").value("01045444544"));
    }

    @Test
    void signAndLoginTest() throws Exception {
        MemberRequestDTO memberRequestDTO = new MemberRequestDTO();
        memberRequestDTO.setArea("대덕");
        memberRequestDTO.setMemberName("mj");
        memberRequestDTO.setAuth(MemberRole.PASSENGER);
        memberRequestDTO.setDepartment("software");
        memberRequestDTO.setEmail("abc@abc.com");
        memberRequestDTO.setPassword("2011");
        memberRequestDTO.setPhoneNumber("01045444544");
        memberRequestDTO.setMemberTimeTable(new ArrayList<>());

        ResultActions perform = this.mockMvc.perform(post("/auth/signup").
                contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(memberRequestDTO)));

        ResultActions perform2 = this.mockMvc.perform(post("/auth/login").
                contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(memberRequestDTO)));
        perform2.andExpect(status().isOk());

    }
}
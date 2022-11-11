package com.example.eunboard.controller;

import com.example.eunboard.domain.dto.QuestionBoardDTO;
import com.example.eunboard.domain.dto.ReportBoardDTO;
import com.example.eunboard.domain.dto.request.MemberRequestDTO;
import com.example.eunboard.domain.dto.response.MemberResponseDTO;
import com.example.eunboard.domain.entity.Member;
import com.example.eunboard.domain.entity.QuestionBoard;
import com.example.eunboard.domain.entity.ReportBoard;
import com.example.eunboard.service.MemberService;
import com.example.eunboard.service.QuestionBoardService;
import com.example.eunboard.service.ReportBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Slf4j
@ResponseBody
@RequiredArgsConstructor
@RestController
public class ReportController {


    private final ReportBoardService reportBoardService;

    private final QuestionBoardService questionBoardService;

    private final MemberService memberService;

    @GetMapping("/ReportBoard")
    public ResponseEntity<?> selectReportForm(@AuthenticationPrincipal long memberId ) {

        List<ReportBoard> Boards = reportBoardService.findByMemberId(memberId);

        if(Boards.size() == 0)
            return ResponseEntity.ok().body("null");

        return ResponseEntity.ok().body(Boards);
    }

    @PostMapping("/ReportBoard/new")
    public String updateReport(@AuthenticationPrincipal long memberId,
                               @RequestBody ReportBoardDTO requestDTO) {

        MemberResponseDTO member= memberService.select(memberId);
        requestDTO.setWriterStudentId(member.getStudentNumber());
        requestDTO.setMemberId(memberId);

        reportBoardService.createReportBoard(requestDTO);

        return "ReportBoard save ok";
    }

    @GetMapping("/QuestionBoard")
    public ResponseEntity<?> selectQuestionForm(@AuthenticationPrincipal long memberId ) {

        List<QuestionBoard> Boards = questionBoardService.findByMemberId(memberId);

        if(Boards.size() == 0)
            return ResponseEntity.ok().body("null");

        return ResponseEntity.ok().body(Boards);
    }

    @PostMapping("/QuestionBoard/new")
    public String updateQuestion(@AuthenticationPrincipal long memberId,
                                 @RequestBody QuestionBoardDTO requestDTO) {

        MemberResponseDTO member= memberService.select(memberId);
        requestDTO.setWriterStudentId(member.getStudentNumber());
        requestDTO.setMemberId(memberId);

        questionBoardService.createQuestionBoard(requestDTO);

        return "QuestionBoard save ok";
    }
}

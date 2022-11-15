package com.example.eunboard.old.controller;

import com.example.eunboard.old.domain.dto.QuestionBoardDTO;
import com.example.eunboard.old.domain.dto.ReportBoardDTO;
import com.example.eunboard.member.application.port.in.MemberResponseDTO;
import com.example.eunboard.old.domain.entity.QuestionBoard;
import com.example.eunboard.old.domain.entity.ReportBoard;
import com.example.eunboard.member.application.service.MemberService;
import com.example.eunboard.old.service.QuestionBoardService;
import com.example.eunboard.old.service.ReportBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

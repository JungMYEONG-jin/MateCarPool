package com.example.eunboard.board.adapter.in;

import com.example.eunboard.board.application.port.in.*;
import com.example.eunboard.board.domain.ReportBoard;
import com.example.eunboard.member.application.port.in.MemberResponseDTO;
import com.example.eunboard.member.application.port.in.MemberUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@ResponseBody
@RequiredArgsConstructor
@RestController
public class ReportController {

    private final ReportBoardUseCase reportBoardService;
    private final QuestionBoardUseCase questionBoardService;
    private final MemberUseCase memberService;


    @GetMapping("/reports")
    public ResponseEntity<List<ReportBoard>> selectReportForm(@AuthenticationPrincipal UserDetails userDetails) {
        long memberId = Long.parseLong(userDetails.getUsername());
        List<ReportBoard> Boards = reportBoardService.findByMemberId(memberId);
        return ResponseEntity.ok().body(Boards);
    }

    @PostMapping("/report/new")
    public ResponseEntity<String> updateReport(@AuthenticationPrincipal long memberId,
                                               @RequestBody ReportBoardDTO requestDTO) {
        MemberResponseDTO member = memberService.select(memberId);
        requestDTO.setWriterStudentId(member.getStudentNumber());
        requestDTO.setMemberId(memberId);
        reportBoardService.createReportBoard(requestDTO);
        return ResponseEntity.ok("ReportBoard save ok");
    }

    @GetMapping("/question")
    public ResponseEntity<List<QuestionBoardShowDto>> selectQuestionForm(@AuthenticationPrincipal UserDetails userDetails) {
        long memberId = Long.parseLong(userDetails.getUsername());
        memberService.checkMember(memberId);
        return ResponseEntity.ok(questionBoardService.findByMemberId(memberId));
    }

    @PostMapping("/question/new")
    public ResponseEntity<String> enrollQuestion(@AuthenticationPrincipal UserDetails userDetails,
                                                 @RequestBody QuestionBoardEnrollDTO requestDTO) {
        long memberId = Long.parseLong(userDetails.getUsername());
        memberService.checkMember(memberId);
        requestDTO.setMemberId(memberId);
        requestDTO.setWriterStudentId(memberService.select(memberId).getStudentNumber());
        questionBoardService.createQuestionBoard(requestDTO);
        return ResponseEntity.ok("1:1 문의에 성공하였습니다.");
    }
}

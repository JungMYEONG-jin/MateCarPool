package com.example.eunboard.board.adapter.in;

import com.example.eunboard.board.application.port.in.QuestionBoardDTO;
import com.example.eunboard.board.application.port.in.QuestionBoardUseCase;
import com.example.eunboard.board.application.port.in.ReportBoardDTO;
import com.example.eunboard.board.application.port.in.ReportBoardUseCase;
import com.example.eunboard.member.application.port.in.MemberResponseDTO;
import com.example.eunboard.board.domain.QuestionBoard;
import com.example.eunboard.board.domain.ReportBoard;
import com.example.eunboard.member.application.port.in.MemberUseCase;
import com.example.eunboard.member.application.service.MemberService;
import com.example.eunboard.board.application.service.QuestionBoardService;
import com.example.eunboard.board.application.service.ReportBoardService;
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

    private final ReportBoardUseCase reportBoardService;
    private final QuestionBoardUseCase questionBoardService;
    private final MemberUseCase memberService;


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
        log.info("phone {}", memberId);
        List<QuestionBoard> Boards = questionBoardService.findByMemberId(memberId);

        if(Boards.size() == 0)
            return ResponseEntity.ok().body("null");

        return ResponseEntity.ok().body(Boards);
    }

    @PostMapping("/QuestionBoard/new")
    public String updateQuestion(@AuthenticationPrincipal long memberId,
                                 @RequestBody QuestionBoardDTO requestDTO) {
        log.info("phone {}", memberId);
        MemberResponseDTO member= memberService.select(memberId);
        requestDTO.setWriterStudentId(member.getStudentNumber());
        requestDTO.setMemberId(memberId);
        questionBoardService.createQuestionBoard(requestDTO);

        return "QuestionBoard save ok";
    }
}

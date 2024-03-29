package com.example.eunboard.board.adapter.in;

import com.example.eunboard.board.application.port.in.*;
import com.example.eunboard.board.domain.ReportBoard;
import com.example.eunboard.member.application.port.in.MemberUseCase;
import com.example.eunboard.shared.common.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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

    @Parameter(name = "userDetails", hidden = true)
    @Operation(summary = "신고하기", description = "다른 사람을 신고할 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "신고 완료", content = @Content(schema = @Schema(implementation = CommonResponse.class)))})
    @PostMapping("/report/new")
    public ResponseEntity<CommonResponse> createReport(@AuthenticationPrincipal UserDetails userDetails,
                                                       @RequestBody ReportCreateRequestDTO requestDTO) {
        long memberId = Long.parseLong(userDetails.getUsername());
        requestDTO.setMemberId(memberId);
        reportBoardService.createReportBoard(requestDTO);

        CommonResponse res = CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .message("정상적으로 신고가 완료 되었습니다.")
                .build();
        return ResponseEntity.ok(res);
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

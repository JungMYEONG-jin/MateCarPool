package com.example.eunboard.board.application.port.in;

import com.example.eunboard.board.domain.ReportBoard;

import java.util.List;

public interface ReportBoardUseCase {
    void createReportBoard(ReportCreateRequestDTO reportDto);
    //검색
    List<ReportBoard> findByMemberId(long memberId);
}

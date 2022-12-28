package com.example.eunboard.board.application.port.out;

import com.example.eunboard.board.domain.ReportBoard;

import java.util.List;

public interface ReportBoardRepositoryPort {
    List<ReportBoard> findByMemberId(Long memberId);
    ReportBoard save(ReportBoard reportBoard);
}
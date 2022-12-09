package com.example.eunboard.board.adapter.out;

import com.example.eunboard.board.application.port.out.ReportBoardRepositoryPort;
import com.example.eunboard.board.domain.ReportBoard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReportBoardRepositoryAdapter implements ReportBoardRepositoryPort {

    private final ReportBoardRepository reportBoardRepository;

    @Override
    public List<ReportBoard> findByMemberId(Long memberId) {
        return reportBoardRepository.findByMemberId(memberId);
    }

    @Override
    public ReportBoard save(ReportBoard reportBoard) {
        return reportBoardRepository.save(reportBoard);
    }
}

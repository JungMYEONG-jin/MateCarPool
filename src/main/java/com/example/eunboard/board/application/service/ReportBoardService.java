package com.example.eunboard.board.application.service;

import com.example.eunboard.board.application.port.in.ReportBoardDTO;
import com.example.eunboard.board.application.port.in.ReportBoardUseCase;
import com.example.eunboard.board.application.port.out.ReportBoardRepositoryPort;
import com.example.eunboard.board.domain.ReportBoard;
import com.example.eunboard.board.adapter.out.ReportBoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class ReportBoardService implements ReportBoardUseCase {

    private final ReportBoardRepositoryPort reportBoardRepository;

    // 티켓 생성
    @Override
    public void createReportBoard(ReportBoardDTO reportDto) {
        ReportBoard report = ReportBoardDTO.toReportEntity(reportDto);
        reportBoardRepository.save(report);
    }

    //검색
    @Override
    public List<ReportBoard> findByMemberId(long memberId) {
        return reportBoardRepository.findByMemberId(memberId);
    }
}

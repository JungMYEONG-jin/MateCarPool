package com.example.eunboard.old.service;

import com.example.eunboard.old.domain.dto.ReportBoardDTO;
import com.example.eunboard.old.domain.entity.ReportBoard;
import com.example.eunboard.old.domain.repository.board.ReportBoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReportBoardService {

    private final ReportBoardRepository reportBoardRepository;

    // 티켓 생성
    public void createReportBoard(ReportBoardDTO reportDto) {
        ReportBoard report = ReportBoardDTO.toReportEntity(reportDto);
        reportBoardRepository.save(report);
    }

    //검색
    public List<ReportBoard> findByMemberId(long memberId) {
        return reportBoardRepository.findByMemberId(memberId);
    }
}

package com.example.eunboard.board.application.service;

import com.example.eunboard.board.application.port.in.ReportBoardDTO;
import com.example.eunboard.board.application.port.in.ReportBoardUseCase;
import com.example.eunboard.board.application.port.in.ReportCreateRequestDTO;
import com.example.eunboard.board.application.port.out.ReportBoardRepositoryPort;
import com.example.eunboard.board.domain.ReportBoard;
import com.example.eunboard.member.adapter.out.repository.MemberRepository;
import com.example.eunboard.member.application.port.out.MemberRepositoryPort;
import com.example.eunboard.member.domain.Member;
import com.example.eunboard.shared.exception.ErrorCode;
import com.example.eunboard.shared.exception.custom.CustomException;
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
    private final MemberRepositoryPort memberRepository;
    // 티켓 생성
    @Override
    public void createReportBoard(ReportCreateRequestDTO reportDto) {

        Member member = memberRepository.findByMemberId(reportDto.getMemberId());
        if(member == null)
            throw new  CustomException(ErrorCode.MEMBER_NOT_FOUND.getMessage(), ErrorCode.MEMBER_NOT_FOUND);

        reportDto.setWriterStudentId(member.getStudentNumber());
        ReportBoard report = reportDto.toEntity();
        reportBoardRepository.save(report);
    }

    //검색
    @Override
    public List<ReportBoard> findByMemberId(long memberId) {
        return reportBoardRepository.findByMemberId(memberId);
    }
}

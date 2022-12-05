package com.example.eunboard.ticket.application.port.in;

import com.example.eunboard.ticket.domain.TicketStatus;

import java.util.List;

public interface TicketUseCase {
    // 티켓 생성
    void createCarPool(TicketCreateRequestDto requestDto);
    // 티켓 목록 조회
    List<TicketShortResponseDto> getCarPoolList();
    // 티켓 상세보기
    TicketDetailResponseDto readTicket(Long id);
    // 티켓 상태 업데이트
    void ticketStatusUpdate(long memberId, long id, TicketStatus status);
    TicketDetailResponseDto getPromise(Long memberId);
    List<TicketDetailResponseDto> getPromises(Long memberId);
}

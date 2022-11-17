package com.example.eunboard.ticket.application.port.in;

import com.example.eunboard.ticket.domain.TicketStatus;

import java.util.List;

public interface TicketUseCase {
    // 티켓 생성
    void ticketSave(TicketRequestDTO requestDTO);
    // 티켓 목록 조회
    List<TicketResponseDTO> findAll();
    // 티켓 상세보기
    TicketResponseDTO ticketRead(Long id);
    // 티켓 상태 업데이트
    void ticketStatusUpdate(long id, TicketStatus status);
    TicketResponseDTO ticketPromise(Long memberId);
}

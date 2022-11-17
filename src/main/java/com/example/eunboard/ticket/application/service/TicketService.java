package com.example.eunboard.ticket.application.service;

import com.example.eunboard.member.adapter.out.repository.MemberRepository;
import com.example.eunboard.member.application.port.out.MemberRepositoryPort;
import com.example.eunboard.member.domain.Member;
import com.example.eunboard.shared.exception.ErrorCode;
import com.example.eunboard.shared.exception.custom.CustomException;
import com.example.eunboard.ticket.adapter.out.CustomTicketRepositoryImpl;
import com.example.eunboard.ticket.application.port.in.TicketRequestDTO;
import com.example.eunboard.ticket.application.port.in.TicketResponseDTO;
import com.example.eunboard.ticket.application.port.in.TicketUseCase;
import com.example.eunboard.ticket.application.port.out.TicketRepositoryPort;
import com.example.eunboard.ticket.domain.Ticket;
import com.example.eunboard.ticket.domain.TicketStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class TicketService implements TicketUseCase {
    private final TicketRepositoryPort ticketRepository;
    private final MemberRepositoryPort memberRepository;

    // 티켓 생성
    @Override
    public void ticketSave(TicketRequestDTO requestDTO) {
        Boolean existTicket = ticketRepository.existTicket(requestDTO.getMemberId());

        if (existTicket == null) {
            throw new CustomException("이미 티켓을 생성하였습니다", ErrorCode.TICKET_IS_EXIST);
        }

        if ( !existTicket) {
            Ticket ticket = TicketRequestDTO.toEntity(requestDTO);
            ticketRepository.save(ticket);
        }

    }

    // 티켓 목록 조회
    @Override
    public List<TicketResponseDTO> findAll() {
        List<Ticket> ticketList = ticketRepository.findAll();
        return ticketList.stream().map(TicketResponseDTO::toDTO).collect(Collectors.toList());
    }

    // 티켓 상세보기
    @Override
    public TicketResponseDTO ticketRead(Long id) {
        Optional<Ticket> ticket = ticketRepository.findById(id);

        if (ticket.isEmpty()) {
            throw new CustomException("티켓을 찾을 수 없습니다.", ErrorCode.TICKET_NOT_FOUND);
        }

        return TicketResponseDTO.toDTO(ticket.get());
    }

    // 티켓 상태 업데이트
    @Override
    public void ticketStatusUpdate(long id, TicketStatus status) {
        Optional<Ticket> ticket = ticketRepository.findById(id);

        if (ticket.isEmpty()) {
            throw new CustomException("티켓을 찾을 수 없습니다.", ErrorCode.TICKET_NOT_FOUND);
        }

        // 정보 업데이트 후 저장
        ticket.get().updateStatus(status);
        ticketRepository.save(ticket.get());
    }

    @Override
    public TicketResponseDTO ticketPromise(Long memberId) {
        Ticket ticket = ticketRepository.findByMember(Member.builder().memberId(memberId).build());

        if (ticket == null) {
            throw new CustomException("티켓을 찾을 수 없습니다.", ErrorCode.TICKET_NOT_FOUND);
        }
        
        return TicketResponseDTO.toDTO(ticket);

    }
}

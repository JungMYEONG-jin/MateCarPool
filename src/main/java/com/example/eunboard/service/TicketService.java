package com.example.eunboard.service;

import com.example.eunboard.domain.dto.request.TicketRequestDTO;
import com.example.eunboard.domain.dto.response.TicketResponseDTO;
import com.example.eunboard.domain.entity.Member;
import com.example.eunboard.domain.entity.Ticket;
import com.example.eunboard.domain.entity.TicketStatus;
import com.example.eunboard.domain.repository.MemberRepository;
import com.example.eunboard.domain.repository.TicketQueryRepository;
import com.example.eunboard.domain.repository.TicketRepository;
import com.example.eunboard.exception.ErrorCode;
import com.example.eunboard.exception.custom.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@Service
public class TicketService {
    private final TicketRepository ticketRepository;
    private final TicketQueryRepository ticketQueryRepository;

    private final MemberRepository memberRepository;

    // 티켓 생성
    public void ticketSave(TicketRequestDTO requestDTO) {
        Boolean existTicket = ticketQueryRepository.existTicket(requestDTO.getMemberId());

        if (existTicket == null) {
            throw new CustomException("이미 티켓을 생성하였습니다", ErrorCode.TICKET_IS_EXIST);
        }

        if ( !existTicket) {
            Ticket ticket = TicketRequestDTO.toEntity(requestDTO);
            ticketRepository.save(ticket);
        }

    }

    // 티켓 목록 조회
    public List<TicketResponseDTO> findAll() {
        List<Ticket> ticketList = ticketQueryRepository.findAll();
        return ticketList.stream().map(TicketResponseDTO::toDTO).collect(Collectors.toList());
    }

    // 티켓 상세보기
    public TicketResponseDTO ticketRead(Long id) {
        Optional<Ticket> ticket = ticketRepository.findById(id);

        if (ticket.isEmpty()) {
            throw new CustomException("티켓을 찾을 수 없습니다.", ErrorCode.TICKET_NOT_FOUND);
        }

        return TicketResponseDTO.toDTO(ticket.get());
    }

    // 티켓 상태 업데이트
    public void ticketStatusUpdate(long id, TicketStatus status) {
        Optional<Ticket> ticket = ticketRepository.findById(id);

        if (ticket.isEmpty()) {
            throw new CustomException("티켓을 찾을 수 없습니다.", ErrorCode.TICKET_NOT_FOUND);
        }

        // 정보 업데이트 후 저장
        ticket.get().updateStatus(status);
        ticketRepository.save(ticket.get());
    }


    public TicketResponseDTO ticketPromise(Long memberId) {
        Ticket ticket = ticketRepository.findByMember(Member.builder().memberId(memberId).build());

        if (ticket == null) {
            throw new CustomException("티켓을 찾을 수 없습니다.", ErrorCode.TICKET_NOT_FOUND);
        }
        
        return TicketResponseDTO.toDTO(ticket);

    }
}

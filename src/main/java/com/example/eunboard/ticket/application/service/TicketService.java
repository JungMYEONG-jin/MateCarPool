package com.example.eunboard.ticket.application.service;

import com.example.eunboard.member.application.port.out.MemberRepositoryPort;
import com.example.eunboard.member.domain.Member;
import com.example.eunboard.member.domain.MemberRole;
import com.example.eunboard.shared.exception.ErrorCode;
import com.example.eunboard.shared.exception.custom.CustomException;
import com.example.eunboard.ticket.application.port.in.TicketCreateRequestDto;
import com.example.eunboard.ticket.application.port.in.TicketDetailResponseDto;
import com.example.eunboard.ticket.application.port.in.TicketShortResponseDto;
import com.example.eunboard.ticket.application.port.in.TicketUseCase;
import com.example.eunboard.ticket.application.port.out.TicketRepositoryPort;
import com.example.eunboard.ticket.domain.Ticket;
import com.example.eunboard.ticket.domain.TicketStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class TicketService implements TicketUseCase {
    private final TicketRepositoryPort ticketRepository;
    private final MemberRepositoryPort memberRepository;

    @Override
    public void createCarPool(TicketCreateRequestDto requestDto) {
        // 시간 체크
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime todayMorning = now.toLocalDate().atTime(9,30);
        LocalDateTime yesterdayNight = now.toLocalDate().atTime(21,0).minusDays(1);

        LocalDateTime todayNight = now.toLocalDate().atTime(21, 0);
        LocalDateTime tomorrowMorning = now.toLocalDate().plusDays(1).atTime(9, 30);

        // 프론트 테스트를 위해 제거
//        if (now.isAfter(yesterdayNight) && now.isBefore(todayMorning) || now.isAfter(todayNight) && now.isBefore(tomorrowMorning))
//            throw new CustomException(ErrorCode.NOT_PERMITTED_TIME.getMessage(), ErrorCode.NOT_PERMITTED_TIME);

        // 드라이버 체크
        Member member = memberRepository.findById(requestDto.getMemberId()).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND.getMessage(), ErrorCode.MEMBER_NOT_FOUND));
        if(member.getAuth().equals(MemberRole.PASSENGER)){
            throw new CustomException(ErrorCode.MEMBER_NOT_AUTHORITY.getMessage(), ErrorCode.MEMBER_NOT_AUTHORITY);
        }
        boolean existTicket = ticketRepository.existTicket(requestDto.getMemberId());
        if (existTicket){
            throw new CustomException(ErrorCode.TICKET_IS_EXIST.getMessage(), ErrorCode.TICKET_IS_EXIST);
        }
        Ticket ticket = TicketCreateRequestDto.toEntity(requestDto);
        ticketRepository.save(ticket);
    }

    // 티켓 목록 조회
    @Override
    public List<TicketShortResponseDto> getCarPoolList() {
        List<Ticket> ticketList = ticketRepository.getAvailableList();
        return ticketList.stream().map(TicketShortResponseDto::toDTO).collect(Collectors.toList());
    }

    // 티켓 상세보기
    @Override
    public TicketDetailResponseDto readTicket(Long id) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow(()->new CustomException(ErrorCode.TICKET_NOT_FOUND.getMessage(), ErrorCode.TICKET_NOT_FOUND));
        return TicketDetailResponseDto.toDTO(ticket);
    }

    // 티켓 상태 업데이트
    @Override
    public void ticketStatusUpdate(long memberId, long id, TicketStatus status) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow(()->new CustomException(ErrorCode.TICKET_NOT_FOUND.getMessage(), ErrorCode.TICKET_NOT_FOUND));
        if (!ticket.getMember().getMemberId().equals(memberId))
            throw new CustomException(ErrorCode.MEMBER_NOT_AUTHORITY.getMessage(), ErrorCode.MEMBER_NOT_AUTHORITY);
        // 정보 업데이트 후 저장
        ticket.updateStatus(status);
        ticketRepository.save(ticket);
    }

    @Override
    public TicketDetailResponseDto getPromise(Long memberId) {
        Ticket ticket = ticketRepository.findByMember(Member.builder().memberId(memberId).build());

        if (ticket == null) {
            throw new CustomException(ErrorCode.TICKET_NOT_FOUND.getMessage(), ErrorCode.TICKET_NOT_FOUND);
        }
        
        return TicketDetailResponseDto.toDTO(ticket);
    }

    /**
     * 카풀 만약 여러개 운행이 가능하다면
     * @param memberId
     * @return
     */
    @Override
    public List<TicketDetailResponseDto> getPromises(Long memberId) {
        List<Ticket> result = ticketRepository.findByMemberId(memberId);
        if (result.isEmpty())
            throw new CustomException(ErrorCode.TICKET_NOT_FOUND.getMessage(), ErrorCode.TICKET_NOT_FOUND);
        return result.stream().map(TicketDetailResponseDto::toDTO).collect(Collectors.toList());
    }


}

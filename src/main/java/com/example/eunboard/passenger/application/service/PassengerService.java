package com.example.eunboard.passenger.application.service;

import com.example.eunboard.member.application.port.out.MemberRepositoryPort;
import com.example.eunboard.member.domain.Member;
import com.example.eunboard.passenger.application.port.in.PassengerCreateRequestDTO;
import com.example.eunboard.passenger.application.port.in.PassengerDeleteRequestDTO;
import com.example.eunboard.passenger.application.port.in.PassengerUseCase;
import com.example.eunboard.passenger.application.port.out.PassengerRepositoryPort;
import com.example.eunboard.passenger.domain.Passenger;
import com.example.eunboard.shared.exception.ErrorCode;
import com.example.eunboard.shared.exception.custom.CustomException;
import com.example.eunboard.ticket.application.port.out.TicketRepositoryPort;
import com.example.eunboard.ticket.domain.Ticket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class PassengerService implements PassengerUseCase {

    private final PassengerRepositoryPort passengerRepository;
    private final TicketRepositoryPort ticketRepositoryPort;
    private final MemberRepositoryPort memberRepository;

    /**
     * 탑승 가능한 적정 인원인지 체크 필요.
     *
     * @param requestDTO
     */
    public void ride(PassengerCreateRequestDTO requestDTO) {
        // 다른 카풀에 신청한게 있는지 확인
        if (passengerRepository.existBoardingStatePassenger(requestDTO.getMemberId()))
            throw new CustomException(ErrorCode.TICKET_PASS_EXIST.getMessage(), ErrorCode.TICKET_PASS_EXIST);

        /**
         * 이미 현재 카풀 신청한게 있는지 체크
         */
        if (passengerRepository.findRide(PassengerCreateRequestDTO.toEntity(requestDTO))) {
            throw new CustomException(ErrorCode.TICKET_PASS_EXIST.getMessage(), ErrorCode.TICKET_PASS_EXIST);
        }
        /**
         * 티켓에 자리가 남았는지 체크
         */
        Ticket ticket = ticketRepositoryPort.findById(requestDTO.getTicketId()).orElseThrow(() -> new CustomException(ErrorCode.TICKET_NOT_FOUND.getMessage(), ErrorCode.TICKET_NOT_FOUND));

        if (ticket.getPassengerList().stream().filter(passenger -> !passenger.isCancel()).count() == ticket.getRecruitPerson()) {
            throw new CustomException(ErrorCode.TICKET_IS_FULL.getMessage(), ErrorCode.TICKET_IS_FULL);
        }
        passengerRepository.save(PassengerCreateRequestDTO.toEntity(requestDTO));
    }

    /**
     *
     * @param requestMemberId 탑승자를 지워달라고 요청을 보낸 사용자 id
     * @param targetPassengerId 티켓에서 지워야하는 탑승자의 id
     */

    public void takeDown(long requestMemberId, long targetPassengerId, long ticketId) {
        Member requestMember = memberRepository.findById(requestMemberId)
                .orElseThrow(()-> new CustomException(ErrorCode.MEMBER_NOT_FOUND.getMessage(), ErrorCode.MEMBER_NOT_FOUND));

        Ticket ticket = ticketRepositoryPort
                .findById(ticketId)
                .orElseThrow(()-> new CustomException(ErrorCode.TICKET_NOT_FOUND.getMessage(), ErrorCode.TICKET_NOT_FOUND));

        Passenger passenger = passengerRepository.findByTicketIdAndPassengerId(ticketId, targetPassengerId)
                .orElseThrow(()-> new CustomException(ErrorCode.TICKET_PASS_NOT_FOUND.getMessage(), ErrorCode.TICKET_PASS_NOT_FOUND));

        switch(requestMember.getAuth()){
            case PASSENGER:
                // 탑승자 memberId가 passengerId와 동일한지 확인
                System.out.println(passenger.getMember().getMemberId());
                System.out.println(requestMember.getMemberId());
                if(!passenger.getMember().getMemberId().equals(requestMember.getMemberId())){
                    throw new CustomException(ErrorCode.MEMBER_NOT_AUTHORITY.getErrorCode(), ErrorCode.MEMBER_NOT_AUTHORITY);
                }
                passenger.cancel();
                break;
            case DRIVER:
                // 드라이버가 자신의 카풀이 맞는지 확인해야함.
                if(!Objects.equals(ticket.getMember().getMemberId(), requestMember.getMemberId())){
                    throw new CustomException(ErrorCode.MEMBER_NOT_AUTHORITY.getErrorCode(), ErrorCode.MEMBER_NOT_AUTHORITY);
                }
                passenger.cancel(); // 현재 탑승자를 취소 상태 변경
                break;
            default:
        }
    }
}

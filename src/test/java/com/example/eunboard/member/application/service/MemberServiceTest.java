package com.example.eunboard.member.application.service;

import com.example.eunboard.member.application.port.out.MemberRepositoryPort;
import com.example.eunboard.member.domain.Member;
import com.example.eunboard.member.domain.MemberRole;
import com.example.eunboard.passenger.application.port.out.PassengerRepositoryPort;
import com.example.eunboard.passenger.domain.Passenger;
import com.example.eunboard.ticket.application.port.out.TicketRepositoryPort;
import com.example.eunboard.ticket.domain.Ticket;
import com.example.eunboard.timetable.application.port.out.MemberTimetableRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepositoryPort memberRepository;
    @Mock
    private PassengerRepositoryPort passengerRepositoryPort;
    @Mock
    private MemberTimetableRepositoryPort memberTimetableRepositoryPort;
    @Mock
    private TicketRepositoryPort ticketRepositoryPort;

    @Test
    @DisplayName("회원 탈퇴를 하는 경우, 기존에 가지고 있던 티켓을 삭제하고 탑승자들을 탑승 취소한다.")
    public void test1() throws Exception {

        Long memberId = 53452353L;
        Member member = mock(Member.class);
        Ticket ticket = mock(Ticket.class);
        Passenger passenger = mock(Passenger.class);

        given(memberRepository.findById(memberId)).willReturn(Optional.ofNullable(member));
        given(ticketRepositoryPort.existTicket(memberId)).willReturn(true); // 기존에 가지고 있던 카풀이 있을 경우
        given(ticketRepositoryPort.findByMember(any())).willReturn(ticket);
        given(passengerRepositoryPort.findAllByTicket(any())).willReturn(List.of(passenger)); // 카풀에 탑승해있는 탑승자 리스트 반환
        given(member.getAuth()).willReturn(MemberRole.DRIVER); // 사용자가 드라이버인 경우

        memberService.delete(memberId);

        verify(memberRepository, times(1)).findById(memberId);
        verify(passenger, times(1)).cancel();
        verify(member, times(1)).delete();
    }
}
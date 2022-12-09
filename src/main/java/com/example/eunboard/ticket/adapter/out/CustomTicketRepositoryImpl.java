package com.example.eunboard.ticket.adapter.out;

import com.example.eunboard.ticket.domain.Ticket;
import com.example.eunboard.ticket.domain.TicketStatus;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.eunboard.member.domain.QMember.member;
import static com.example.eunboard.passenger.domain.QPassenger.passenger;
import static com.example.eunboard.ticket.domain.QTicket.ticket;

@Transactional
@RequiredArgsConstructor
@Slf4j
public class CustomTicketRepositoryImpl implements CustomTicketRepository{

    private final JPAQueryFactory queryFactory;

    private BooleanExpression statusEq(TicketStatus ticketStatus){
        return ticket.status.eq(ticketStatus);
    }

    private BooleanExpression statusEqNot(TicketStatus ticketStatus){
        return ticket.status.eq(ticketStatus).not();
    }


    @Transactional(readOnly = true)
    @Override
    public List<Ticket> findAll() {
        return queryFactory
                .selectFrom(ticket)
                .leftJoin(ticket.passengerList, passenger)
                .fetchJoin()
                .leftJoin(ticket.member, member)
                .fetchJoin()
                .where(statusEqNot(TicketStatus.AFTER).and(statusEqNot(TicketStatus.CANCEL)))
                .fetch();
    }

    /**
     * 우선 9시30~ 오후9시 까지 생성 만족 뽑음
     * 만약 만족 하는게 있다면 현재 시간이 만족일 오후9시 ~ 만족다음일 오전9시30까지 만족하면 값 리턴함
     * 1 현재가 9시30 이후일경우 night는 저렇게 쓰고
     * 현재가 오전9시30 이전일경우 morning -1 night -1 일씩해주자.
     * @return
     */
    @Override
    public List<Ticket> getAvailableList() {
        LocalDateTime now = LocalDateTime.now();
        // 만약 새벽~오전 9시30분 전이면 -1씩 해야함
        // 전날 오전9시30~오후9시까지 생성된 티켓을 봐야하기 때문.
        LocalDateTime morning = now.toLocalDate().atTime(9, 30);
        LocalDateTime night = now.toLocalDate().atTime(21, 0);
        if (now.isAfter(now.toLocalDate().atTime(0,0)) && now.isBefore(now.toLocalDate().atTime(9, 30))){
            morning = morning.minusDays(1L);
            night = night.minusDays(1L);
        }
        // 우선 9시30 오후 9시까지 생성된 리스트를 뽑는다.
        List<Ticket> filtered = queryFactory.selectFrom(ticket)
                .leftJoin(ticket.passengerList, passenger)
                .fetchJoin()
                .leftJoin(ticket.member, member)
                .fetchJoin()
                .where(statusEqNot(TicketStatus.AFTER).and(statusEqNot(TicketStatus.CANCEL))
                ).fetch();
        return filtered;
        // 프론트 테스트 위해 잠시 제거
        // ticket.createDate.between(morning, night)
        // 리스트를 생성일 오후9시 ~ 생성 다음일 오전 9시 30분까지 보여준다.

//        if (filtered!=null) {
//            LocalDateTime start = filtered.get(0).getCreateDate().toLocalDate().atTime(21, 0);
//            LocalDateTime end = filtered.get(0).getCreateDate().toLocalDate().plusDays(1).atTime(9, 30);
//            if (now.isAfter(start) && now.isBefore(end))
//                return filtered;
//        }
//        return new ArrayList<>();
    }

    @Override
    public boolean existTicket(Long memberId) {
        return queryFactory
                .selectFrom(ticket)
                .where((statusEq(TicketStatus.BEFORE)
                        .or(statusEq(TicketStatus.ING)))
                        .and(ticket.member.memberId.eq(memberId)))
                .fetchOne() != null;
    }
}


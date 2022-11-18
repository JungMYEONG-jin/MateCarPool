package com.example.eunboard.ticket.adapter.out;

import com.example.eunboard.ticket.domain.Ticket;
import com.example.eunboard.ticket.domain.TicketStatus;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

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
                .where(statusEqNot(TicketStatus.AFTER))
                .fetch();
    }

    @Override
    public Boolean existTicket(Long memberId) {
        return queryFactory
                .selectFrom(ticket)
                .where(ticket.status.eq(TicketStatus.BEFORE)
                        .and(statusEq(TicketStatus.ING))
                        .and(ticket.member.memberId.eq(memberId)))
                .fetchOne() != null;
    }
}
package com.example.eunboard.passenger.adapter.out.repository;

import com.example.eunboard.member.domain.Member;
import com.example.eunboard.passenger.domain.Passenger;
import com.example.eunboard.ticket.domain.Ticket;
import com.example.eunboard.ticket.domain.TicketStatus;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.example.eunboard.member.domain.QMember.member;
import static com.example.eunboard.passenger.domain.QPassenger.passenger;
import static com.example.eunboard.ticket.domain.QTicket.ticket;

@Transactional
@RequiredArgsConstructor
public class CustomPassengerRepositoryImpl implements CustomPassengerRepository {

    private final JPAQueryFactory queryFactory;

    private BooleanExpression eqMember(Member member) {
        return passenger.member.eq(member);
    }

    private BooleanExpression eqTicket(Ticket ticket) {
        return passenger.ticket.eq(ticket);
    }

    /**
     * member, ticket 같으며 cancel이 0 인 상태.
     * 즉 탑승 신청이 완료된 상태
     *
     * @param entity
     * @return
     */
    @Override
    public boolean findRide(Passenger entity) {
        return queryFactory
                .selectFrom(passenger)
                .where(eqMember(entity.getMember()),
                        eqTicket(entity.getTicket()),
                        passenger.isCancel.eq(0))
                .fetch().size() > 0;
        //passenger.isCancel.eq(0)
    }

    @Override
    public Passenger findMyPassenger(Passenger entity) {
        return queryFactory
                .selectFrom(passenger)
                .leftJoin(passenger.member, member)
                .fetchJoin()
                .leftJoin(passenger.ticket, ticket)
                .fetchJoin()
                .where(eqMember(entity.getMember()),
                        passenger.id.eq(entity.getId()))
                .fetchOne();
    }

    @Override
    public List<Passenger> getBoardingList(Long memberId) {
        return queryFactory.selectFrom(passenger)
                .leftJoin(passenger.member, member)
                .fetchJoin()
                .leftJoin(passenger.ticket, ticket)
                .fetchJoin()
                .where(passenger.member.memberId.eq(memberId),
                        passenger.isCancel.eq(0))
                .orderBy(passenger.ticket.createDate.desc())
                .fetch();
    }

    @Override
    public boolean existBoardingStatePassenger(Long memberId) {
        // 현재 사용자가 탑승한 티켓이 있는지 확인하는 로직
        return queryFactory.select(passenger)
                .from(passenger)
                .innerJoin(ticket).fetchJoin()
                .on(passenger.ticket.id.eq(ticket.id)) //
                .where(passenger.member.memberId.eq(memberId))
                .where(passenger.isCancel.eq(0)).fetchOne() != null;
    }

    @Override
    public Passenger findCurrentPassengerByMember(Member member) {
        // 시간 역순으로 한뒤 가장 최근의 passenger 내역을 조회
        return queryFactory.select(passenger)
                .from(passenger)
                .where(passenger.member.eq(member))
                .where(passenger.ticket.status.eq(TicketStatus.BEFORE).and(passenger.ticket.status.eq(TicketStatus.ING)))
                .orderBy(passenger.ticket.createDate.desc())
                .fetchFirst();
    }

    @Override
    public Optional<Passenger> findByTicketIdAndPassengerId(long ticketId, long passengerId) {
        return Optional.ofNullable(queryFactory.select(passenger)
                .from(passenger)
                .where(passenger.ticket.id.eq(ticketId))
                .where(passenger.id.eq(passengerId))
                .orderBy(passenger.createDate.desc())
                .fetchOne());
    }


    /**
     * 해당 로직은 'Passenger' 만 사용해야 합니다.
     * Passenger가 탑승하고있는 카풀 중 BEFORE, ING 상태인 카풀 중 가장 최근 것을 '하나' 불러옵니다.
     * @param memberId 현재 사용자 아이디(학번과 다름)
     * @return 만약 카풀이 존재한다면 Passenger, 없다면 null
     * @author tianea
     */
    @Override
    public Passenger findMyPassengerByMemberId(Long memberId) {
        return queryFactory.select(passenger)
                .from(passenger)
                .where(passenger.member.memberId.eq(memberId)) // 현재 탑승자와 사용자의 멤버 아이디가 같아야하고
                .where(passenger.isCancel.eq(0)) // 탑승자가 취소된 상태가 아니여야함
                .where(passenger.ticket.status.in(List.of(TicketStatus.BEFORE, TicketStatus.ING))) // 탑승한 티켓의 상태가 출발전이나 출발 도중이여야함.
                .orderBy(passenger.createDate.desc())
                .fetchFirst();
    }
}

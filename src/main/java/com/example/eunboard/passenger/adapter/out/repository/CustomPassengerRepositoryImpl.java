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
public class CustomPassengerRepositoryImpl implements CustomPassengerRepository{

  private final JPAQueryFactory queryFactory;

  private BooleanExpression eqMember(Member member){
      return passenger.member.eq(member);
  }
  private BooleanExpression eqTicket(Ticket ticket) {return passenger.ticket.eq(ticket);}

  /**
   * member, ticket 같으며 cancel이 0 인 상태.
   * 즉 탑승 신청이 완료된 상태
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
}

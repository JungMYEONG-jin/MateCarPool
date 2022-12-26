package com.example.eunboard.passenger.adapter.out.repository;

import com.example.eunboard.member.domain.Member;
import com.example.eunboard.passenger.domain.Passenger;
import com.example.eunboard.ticket.domain.Ticket;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

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
            .fetch();
  }
}

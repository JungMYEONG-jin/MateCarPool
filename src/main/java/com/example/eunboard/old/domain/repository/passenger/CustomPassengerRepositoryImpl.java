package com.example.eunboard.old.domain.repository.passenger;

import com.example.eunboard.old.domain.entity.Passenger;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;


import static com.example.eunboard.member.domain.QMember.member;
import static com.example.eunboard.old.domain.entity.QPassenger.passenger;
import static com.example.eunboard.old.domain.entity.QTicket.ticket;

@Transactional
@RequiredArgsConstructor
public class CustomPassengerRepositoryImpl implements CustomPassengerRepository{

  private final JPAQueryFactory queryFactory;

  private BooleanExpression eqMember(Passenger entity){
      return passenger.member.eq(entity.getMember());
  }

  @Override
  public boolean findRide(Passenger entity) {
    return queryFactory
        .selectFrom(passenger)
        .where(eqMember(entity),
            passenger.isCancel.eq(0))
        .fetch().size() > 0;
  }

  @Override
  public Passenger findMyPassenger(Passenger entity) {
    return queryFactory
        .selectFrom(passenger)
        .leftJoin(passenger.member, member)
        .fetchJoin()
        .leftJoin(passenger.ticket, ticket)
        .fetchJoin()
        .where(eqMember(entity),
            passenger.id.eq(entity.getId()))
        .fetchOne();
  }
}

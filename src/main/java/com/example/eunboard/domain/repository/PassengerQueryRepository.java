package com.example.eunboard.domain.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.eunboard.domain.entity.Passenger;
import com.querydsl.jpa.impl.JPAQueryFactory;

import static com.example.eunboard.domain.entity.QMember.*;
import static com.example.eunboard.domain.entity.QTicket.*;
import static com.example.eunboard.domain.entity.QPassenger.passenger;

@Transactional
@RequiredArgsConstructor
@Repository
public class PassengerQueryRepository {

  private final JPAQueryFactory queryFactory;

  public boolean findRide(Passenger entity) {
    return queryFactory
        .selectFrom(passenger)
        .where(passenger.member.eq(entity.getMember()),
            passenger.isCancel.eq(0))
        .fetch().size() > 0;
  }

  public Passenger findMyPassenger(Passenger entity) {
    return queryFactory
        .selectFrom(passenger)
        .leftJoin(passenger.member, member)
        .fetchJoin()
        .leftJoin(passenger.ticket, ticket)
        .fetchJoin()
        .where(passenger.member.eq(entity.getMember()),
            passenger.id.eq(entity.getId()))
        .fetchOne();
  }
}

package com.example.eunboard.passenger.adapter.out.repository;

import com.example.eunboard.member.domain.Member;
import com.example.eunboard.passenger.domain.Passenger;

import java.util.List;
import java.util.Optional;

public interface CustomPassengerRepository {
     boolean findRide(Passenger entity);
     Passenger findMyPassenger(Passenger entity);
     /**
      * memberId로 list 추출
      */
     List<Passenger> getBoardingList(Long memberId);

     boolean existBoardingStatePassenger(Long memberId);

     Passenger findCurrentPassengerByMember(Member member);

     Optional<Passenger> findByTicketIdAndPassengerId(long ticketId, long passengerId);

     Passenger findMyPassengerByMemberId(Long memberId);
}

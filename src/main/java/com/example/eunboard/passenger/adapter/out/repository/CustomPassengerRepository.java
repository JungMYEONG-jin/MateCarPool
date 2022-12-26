package com.example.eunboard.passenger.adapter.out.repository;

import com.example.eunboard.passenger.domain.Passenger;

import java.util.List;

public interface CustomPassengerRepository {
     boolean findRide(Passenger entity);
     Passenger findMyPassenger(Passenger entity);
     /**
      * memberId로 list 추출
      */
     List<Passenger> getBoardingList(Long memberId);
}

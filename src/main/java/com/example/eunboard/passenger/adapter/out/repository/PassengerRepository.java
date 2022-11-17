package com.example.eunboard.passenger.adapter.out.repository;

import com.example.eunboard.passenger.domain.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface PassengerRepository extends JpaRepository<Passenger, Long>, CustomPassengerRepository {
}
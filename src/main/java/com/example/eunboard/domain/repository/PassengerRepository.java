package com.example.eunboard.domain.repository;

import com.example.eunboard.domain.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {
}
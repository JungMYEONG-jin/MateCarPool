package com.example.eunboard.old.domain.repository.passenger;

import com.example.eunboard.old.domain.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long>, CustomPassengerRepository {
}
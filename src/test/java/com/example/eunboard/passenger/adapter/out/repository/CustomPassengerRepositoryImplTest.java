package com.example.eunboard.passenger.adapter.out.repository;

import com.example.eunboard.passenger.application.port.out.PassengerRepositoryPort;
import com.example.eunboard.passenger.domain.Passenger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomPassengerRepositoryImplTest {
    @Autowired
    PassengerRepositoryPort passengerRepositoryPort;

    @Test
    void getBoardingTest() {
        List<Passenger> boardingList = passengerRepositoryPort.getBoardingList(34L);
        System.out.println("boardingList = " + boardingList.size());
    }
}
package com.example.eunboard.domain.repository;

import com.example.eunboard.domain.entity.Withdrawal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WithdrawalRepository extends JpaRepository<Withdrawal, Long> {
}
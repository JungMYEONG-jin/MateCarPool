package com.example.eunboard.old.domain.repository.ticket;

import com.example.eunboard.old.domain.entity.Withdrawal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WithdrawalRepository extends JpaRepository<Withdrawal, Long> {
}
package com.example.eunboard.ticket.adapter.out;

import com.example.eunboard.old.domain.entity.Withdrawal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WithdrawalRepository extends JpaRepository<Withdrawal, Long> {
}
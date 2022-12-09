package com.example.eunboard.board.adapter.out.repository;

import com.example.eunboard.board.domain.ReportBoard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportBoardRepository extends JpaRepository<ReportBoard, Long> {

    List<ReportBoard>  findByMemberId(long memberId);
    List<ReportBoard> findByWriterEmail(String email);
}
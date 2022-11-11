package com.example.eunboard.domain.repository;

import com.example.eunboard.domain.entity.QuestionBoard;
import com.example.eunboard.domain.entity.ReportBoard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportBoardRepository extends JpaRepository<ReportBoard, Long> {

    List<ReportBoard>  findByMemberId(long memberId);
    List<ReportBoard> findByWriterEmail(String email);
}
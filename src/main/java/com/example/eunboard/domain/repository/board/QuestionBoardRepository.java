package com.example.eunboard.domain.repository.board;

import com.example.eunboard.domain.entity.QuestionBoard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionBoardRepository extends JpaRepository<QuestionBoard, Long> {
    List<QuestionBoard> findByMemberId(long memberId);
    List<QuestionBoard> findByWriterEmail(String email);
}
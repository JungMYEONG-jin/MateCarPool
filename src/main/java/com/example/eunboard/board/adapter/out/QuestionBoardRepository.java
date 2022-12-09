package com.example.eunboard.board.adapter.out;

import com.example.eunboard.board.domain.QuestionBoard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionBoardRepository extends JpaRepository<QuestionBoard, Long> {
    List<QuestionBoard> findByMemberId(long memberId);
    List<QuestionBoard> findByWriterEmail(String email);
}
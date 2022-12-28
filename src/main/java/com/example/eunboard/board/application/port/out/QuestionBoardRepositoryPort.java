package com.example.eunboard.board.application.port.out;

import com.example.eunboard.board.domain.QuestionBoard;

import java.util.List;

public interface QuestionBoardRepositoryPort {
    List<QuestionBoard> findByMemberId(Long memberId);
    List<QuestionBoard> findByWriterEmail(String writerEmail);
    QuestionBoard save(QuestionBoard questionBoard);
}
package com.example.eunboard.board.application.port.in;

import com.example.eunboard.board.domain.QuestionBoard;

import java.util.List;

public interface QuestionBoardUseCase {
    // 게시판 생성
    void createQuestionBoard(QuestionBoardDTO questionBoardDTO);
    List<QuestionBoard> findByEmail(String email);
    List<QuestionBoard> findByMemberId(long memberId);
}

package com.example.eunboard.board.application.service;

import com.example.eunboard.board.application.port.in.QuestionBoardDTO;
import com.example.eunboard.board.application.port.in.QuestionBoardUseCase;
import com.example.eunboard.board.application.port.out.QuestionBoardRepositoryPort;
import com.example.eunboard.board.domain.QuestionBoard;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class QuestionBoardService implements QuestionBoardUseCase {

    private final QuestionBoardRepositoryPort questionBoardRepository;

    // 게시판 생성
    @Override
    public void createQuestionBoard(QuestionBoardDTO questionBoardDTO) {
        QuestionBoard board = QuestionBoardDTO.toQuestionEntity(questionBoardDTO);
        questionBoardRepository.save(board);
    }

    @Override
    public List<QuestionBoard> findByEmail(String email) {
        return questionBoardRepository.findByWriterEmail(email);
    }

    @Override
    public List<QuestionBoard> findByMemberId(long memberId) {
        return questionBoardRepository.findByMemberId(memberId);
    }
}

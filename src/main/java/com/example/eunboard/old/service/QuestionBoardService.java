package com.example.eunboard.old.service;

import com.example.eunboard.old.domain.dto.QuestionBoardDTO;
import com.example.eunboard.old.domain.entity.QuestionBoard;
import com.example.eunboard.old.domain.repository.board.QuestionBoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class QuestionBoardService {

    private final QuestionBoardRepository questionBoardRepository;

    // 게시판 생성
    public void createQuestionBoard(QuestionBoardDTO questionBoardDTO) {
        QuestionBoard board = QuestionBoardDTO.toQuestionEntity(questionBoardDTO);
        questionBoardRepository.save(board);
    }

    public List<QuestionBoard> findByEmail(String email) {
        return questionBoardRepository.findByWriterEmail(email);
    }

    public List<QuestionBoard> findByMemberId(long memberId) {
        return questionBoardRepository.findByMemberId(memberId);
    }
}

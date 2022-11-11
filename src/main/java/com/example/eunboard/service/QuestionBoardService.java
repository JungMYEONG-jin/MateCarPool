package com.example.eunboard.service;

import com.example.eunboard.domain.dto.QuestionBoardDTO;
import com.example.eunboard.domain.entity.Member;
import com.example.eunboard.domain.entity.QuestionBoard;
import com.example.eunboard.domain.repository.QuestionBoardRepository;
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

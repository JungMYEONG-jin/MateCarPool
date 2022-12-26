package com.example.eunboard.board.application.service;

import com.example.eunboard.board.application.port.in.QuestionBoardEnrollDTO;
import com.example.eunboard.board.application.port.in.QuestionBoardShowDto;
import com.example.eunboard.board.application.port.in.QuestionBoardUseCase;
import com.example.eunboard.board.application.port.out.QuestionBoardRepositoryPort;
import com.example.eunboard.board.domain.QuestionBoard;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class QuestionBoardService implements QuestionBoardUseCase {

    private final QuestionBoardRepositoryPort questionBoardRepository;

    // 게시판 생성
    @Override
    public void createQuestionBoard(QuestionBoardEnrollDTO questionBoardDTO) {
        QuestionBoard board = QuestionBoardEnrollDTO.toQuestionEntity(questionBoardDTO);
        questionBoardRepository.save(board);
    }

    @Override
    public List<QuestionBoard> findByEmail(String email) {
        return questionBoardRepository.findByWriterEmail(email);
    }

    /**
     * US-12 문의 리스트
     * @param memberId
     * @return
     */
    @Override
    public List<QuestionBoardShowDto> findByMemberId(long memberId) {
        List<QuestionBoard> byMemberId = questionBoardRepository.findByMemberId(memberId);
        List<QuestionBoardShowDto> res = new ArrayList<>();
        if (byMemberId!=null)
        {
            res = byMemberId.stream().map(QuestionBoardShowDto::of).collect(Collectors.toList());
        }
        return res;
    }
}

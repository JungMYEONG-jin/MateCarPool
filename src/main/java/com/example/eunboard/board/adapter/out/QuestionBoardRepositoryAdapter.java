package com.example.eunboard.board.adapter.out;

import com.example.eunboard.board.application.port.out.QuestionBoardRepositoryPort;
import com.example.eunboard.board.domain.QuestionBoard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class QuestionBoardRepositoryAdapter implements QuestionBoardRepositoryPort {

    private final QuestionBoardRepository questionBoardRepository;

    @Override
    public List<QuestionBoard> findByMemberId(Long memberId) {
        return questionBoardRepository.findByMemberId(memberId);
    }

    @Override
    public List<QuestionBoard> findByWriterEmail(String writerEmail) {
        return questionBoardRepository.findByWriterEmail(writerEmail);
    }

    @Override
    public QuestionBoard save(QuestionBoard questionBoard) {
        return questionBoardRepository.save(questionBoard);
    }
}

package com.example.eunboard.board.application.port.in;

import com.example.eunboard.board.domain.QuestionBoard;
import lombok.*;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class QuestionBoardShowDto {
    private String title;
    private String content;
    private String writeTime;

    public static QuestionBoardShowDto of(QuestionBoard questionBoard){
        return QuestionBoardShowDto.builder()
                .title(questionBoard.getTitle())
                .content(questionBoard.getContent())
                .writeTime(questionBoard.getCreateDate().format(DateTimeFormatter.ofPattern("yy.MM.dd")))
                .build();
    }
}

package com.example.eunboard.board.application.port.in;
import com.example.eunboard.board.domain.QuestionBoard;
import com.example.eunboard.board.domain.ReportBoard;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.modelmapper.ModelMapper;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class QuestionBoardEnrollDTO {
    private Long id;
    @JsonIgnore
    private Long memberId;
    private String writerEmail;
    private String title;
    private String content;
    @JsonIgnore
    private String writerStudentId;

    public static QuestionBoardEnrollDTO toDTO(ReportBoard entity) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(entity, QuestionBoardEnrollDTO.class);
    }


    public static QuestionBoard toQuestionEntity(QuestionBoardEnrollDTO dto) {
        return QuestionBoard.builder()
                .memberId(dto.memberId)
                .content(dto.getContent())
                .title(dto.getTitle())
                .writerEmail(dto.getWriterEmail())
                .writerStudentId(dto.getWriterStudentId())
                .build();
    }
}

package com.example.eunboard.domain.dto;
import com.example.eunboard.domain.entity.QuestionBoard;
import com.example.eunboard.domain.entity.ReportBoard;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.modelmapper.ModelMapper;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class QuestionBoardDTO {

    private Long id;

    @JsonIgnore
    private Long memberId;

    private String writerStudentId;

    private String writerEmail;

    private String title;

    private String content;

    public static QuestionBoardDTO toDTO(ReportBoard entity) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(entity, QuestionBoardDTO.class);
    }


    public static QuestionBoard toQuestionEntity(QuestionBoardDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(dto, QuestionBoard.class);
    }
}

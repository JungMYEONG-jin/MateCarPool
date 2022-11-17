package com.example.eunboard.board.application.port.in;
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
public class ReportBoardDTO {

    private Long id;

    @JsonIgnore
    private Long memberId;

    private String writerStudentId;

    private String writerEmail;

    private String reportStudentId;

    private String content;

    public static ReportBoardDTO toDTO(ReportBoard entity) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(entity, ReportBoardDTO.class);
    }

    public static ReportBoard toReportEntity(ReportBoardDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(dto, ReportBoard.class);
    }

}

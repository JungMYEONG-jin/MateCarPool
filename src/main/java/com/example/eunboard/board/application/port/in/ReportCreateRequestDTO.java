package com.example.eunboard.board.application.port.in;

import com.example.eunboard.board.domain.ReportBoard;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportCreateRequestDTO {
    @JsonIgnore
    private Long memberId;
    @JsonIgnore
    private String writerStudentId;
    private String reportedStudentId;
    private String content;

    public ReportBoard toEntity() {
        return ReportBoard.builder()
                .memberId(memberId)
                .writerStudentId(writerStudentId)
                .reportStudentId(reportedStudentId)
                .content(content)
                .build();
    }
}

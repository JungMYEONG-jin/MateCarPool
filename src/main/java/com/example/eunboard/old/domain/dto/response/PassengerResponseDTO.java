package com.example.eunboard.old.domain.dto.response;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PassengerResponseDTO {

    private Long ticKetId;

    private String StudentId;

    private String passengerStudentId;


}

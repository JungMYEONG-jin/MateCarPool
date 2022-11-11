package com.example.eunboard.domain.dto.response;
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

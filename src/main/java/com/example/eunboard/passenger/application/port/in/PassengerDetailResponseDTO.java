package com.example.eunboard.passenger.application.port.in;

import com.example.eunboard.member.domain.Member;
import com.example.eunboard.member.domain.MemberRole;
import com.example.eunboard.passenger.domain.Passenger;
import com.example.eunboard.shared.exception.ErrorCode;
import com.example.eunboard.shared.exception.custom.CustomException;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PassengerDetailResponseDTO {
    // 탑승자 번호
    private Long passengerId;

    // 탑승자에 대한 정보
    private String email;

    private String studentNumber;

    private String memberName;

    private String department;

    private String phoneNumber;

    private MemberRole auth;

    private String profileImage;

    private boolean isMember;

    private String area;


    public static PassengerDetailResponseDTO from(Passenger passenger) {
        Member member = passenger.getMember();
        if (member == null)
            throw new CustomException("DTO 변환중에 탑승자에 해당하는 사용자 정보를 찾을 수 없었습니다.", ErrorCode.MEMBER_NOT_FOUND);
        return PassengerDetailResponseDTO.builder()
                .passengerId(passenger.getId())
                .studentNumber(member.getStudentNumber())
                .memberName(member.getMemberName())
                .department(member.getDepartment())
                .phoneNumber(member.getPhoneNumber())
                .auth(member.getAuth())
                .profileImage(member.getProfileImage())
                .isMember(member.isMember())
                .area(member.getArea())
                .build();
    }
}

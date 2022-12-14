package com.example.eunboard.member.application.port.in;

import org.springframework.web.multipart.MultipartFile;

/**
 * use case가 기존에 service에 작성했던 코드 interface 한것
 */
public interface MemberUseCase {
    MemberResponseDTO select(Long id);
    boolean checkRole(Long id);
    void checkMember(Long id);
    void checkStudentNumber(String studentNumber);
    void checkPhoneNumber(String phoneNumber);
    void updateMember(Long memberId, MemberUpdateRequestDTO requestDTO);
    void updateMemberProfileImage(Long memberId, MultipartFile multipartFile);
    MemberUpdateResponseDTO getUpdateView(Long memberId);
    ProfileResponseDto getMyInfoForPassenger(Long memberId);
    ProfileResponseDto getMyInfoForDriver(Long memberId);
    void delete(Long memberId);
}

package com.example.eunboard.member.application.port.in;

import org.springframework.web.multipart.MultipartFile;

/**
 * use case가 기존에 service에 작성했던 코드 interface 한것
 */
public interface MemberUseCase {
    MemberResponseDTO select(Long id);
    boolean checkRole(Long id);
    void checkMember(Long id);
    void updateMember(Long memberId, MultipartFile multipartFile, MemberUpdateRequestDTO requestDTO);
    MemberUpdateResponseDTO getUpdateView(Long memberId);
    ProfileResponseDto getMyInfo(Long memberId);
}

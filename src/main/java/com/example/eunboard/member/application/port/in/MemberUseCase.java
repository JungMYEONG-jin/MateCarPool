package com.example.eunboard.member.application.port.in;

import com.example.eunboard.member.domain.Member;
import org.springframework.web.multipart.MultipartFile;

/**
 * use case가 기존에 service에 작성했던 코드 interface 한것
 */
public interface MemberUseCase {
    MemberResponseDTO select(Long id);
    boolean checkRole(Long id);
    void updateMember(Long memberId, MultipartFile multipartFile, MemberUpdateRequestDTO requestDTO);
    MemberUpdateResponseDTO getUpdateView(Long memberId);
    void updateMemberArea(final Long memberId, final MemberRequestDTO requestDTO);
    MemberResponseDTO getMember(String studentnumber);
    ProfileResponseDto getMyInfo(Long memberId);
}

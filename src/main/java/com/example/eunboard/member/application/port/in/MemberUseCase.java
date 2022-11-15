package com.example.eunboard.member.application.port.in;

import com.example.eunboard.member.domain.Member;

/**
 * use case가 기존에 service에 작성했던 코드 interface 한것
 */
public interface MemberUseCase {
    MemberResponseDTO select(final Long id);
    void updateMember(final Long memberId, final MemberRequestDTO requestDTO);
    void updateProfileImage(final Long memberId, final String fileName);
    Member create(final MemberRequestDTO requestDTO);
    void updateMemberArea(final Long memberId, final MemberRequestDTO requestDTO);
    MemberResponseDTO getMember(String studentnumber);
    MemberResponseDTO getMyInfo();
}

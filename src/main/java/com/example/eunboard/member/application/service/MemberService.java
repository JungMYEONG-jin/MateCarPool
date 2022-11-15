package com.example.eunboard.member.application.service;

import com.example.eunboard.member.application.port.in.MemberRequestDTO;
import com.example.eunboard.member.application.port.in.MemberResponseDTO;
import com.example.eunboard.member.application.port.in.MemberUseCase;
import com.example.eunboard.member.application.port.out.MemberRepositoryPort;
import com.example.eunboard.member.domain.Member;

import com.example.eunboard.exception.ErrorCode;
import com.example.eunboard.exception.custom.CustomException;
import com.example.eunboard.timetable.application.port.out.MemberTimetableRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * author by jmj
 * 헥사고날 방식은 방대해지는 서비스를 줄이는게 목표
 * 그래서 기존 @Service annotation 대신 @Component 대체
 */

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class MemberService implements MemberUseCase {

    private final MemberRepositoryPort memberRepository;
    private final MemberTimetableRepositoryPort memberTimetableRepositoryPort;

    public void copyNonNullProperties(Object src, Object target) {
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }

    public String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null)
                emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    @Override
    public MemberResponseDTO select(final Long id) {
        return MemberResponseDTO.toDTO(memberRepository.findById(id).get(), null);
    }

    @Override
    public void updateMember(final Long memberId, final MemberRequestDTO requestDTO) {
        if (null == requestDTO) {
            throw new RuntimeException("Invalid arguments");
        }
        requestDTO.setMemberId(memberId);
        requestDTO.setMember(true);
        // 존재하지 않는 멤버일시 404 return
        Member member = memberRepository.findById(memberId).orElseThrow(()->new CustomException(ErrorCode.MEMBER_NOT_FOUND.getMessage(), ErrorCode.MEMBER_NOT_FOUND));

        copyNonNullProperties(requestDTO, member);

        memberRepository.save(member);
    }

    @Override
    public void updateProfileImage(final Long memberId, final String fileName) {
        Member member = memberRepository.findById(memberId).orElseThrow(()->new CustomException(ErrorCode.MEMBER_NOT_FOUND.getMessage(), ErrorCode.MEMBER_NOT_FOUND));
        member.setProfileImage(fileName);
        memberRepository.save(member);
    }

    @Override
    public Member create(final MemberRequestDTO requestDTO) {
        if (null == requestDTO || null == requestDTO.getEmail()) {
            throw new RuntimeException("Invalid arguments");
        }
        final String email = requestDTO.getEmail();
        if (memberRepository.existsByEmail(email)) {
            log.debug("MemberService.create Email already exists {}", email);
            return memberRepository.findByEmail(email);
        }
        return memberRepository.save(MemberRequestDTO.toKakaoEntity(requestDTO));
    }

    @Override
    public void updateMemberArea(final Long memberId, final MemberRequestDTO requestDTO) {
        if (null == requestDTO.getArea()) {
            throw new RuntimeException("Invalid arguments");
        }
        Member member = memberRepository.findById(memberId).orElseThrow(()->new CustomException(ErrorCode.MEMBER_NOT_FOUND.getMessage(), ErrorCode.MEMBER_NOT_FOUND));
        member.setArea(requestDTO.getArea());
        memberRepository.save(member);
    }

    @Override
    public MemberResponseDTO getMember(String studentnumber){
        return memberRepository.findByStudentNumber(studentnumber).map(member -> {
                    if (member.getMemberTimeTableList()==null)
                    {
                        return MemberResponseDTO.toDTO(member, null);
                    }
                    return MemberResponseDTO.toDTOWithTimeTable(member, null);
                }).
                orElseThrow(()->new CustomException(ErrorCode.MEMBER_NOT_FOUND.getMessage(), ErrorCode.MEMBER_NOT_FOUND));
    }

    /**
     * jmj
     * 권한 없으면 403, 정보 없으면 404 return
     * @return
     */
    @Override
    public MemberResponseDTO getMyInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null){
            throw new CustomException(ErrorCode.MEMBER_NOT_AUTHORITY.getMessage(), ErrorCode.MEMBER_NOT_AUTHORITY);
        }
        // name is id
        long id = Long.parseLong(authentication.getName());
        return memberRepository.findById(id).map(member -> {
                    if (member.getMemberTimeTableList()==null)
                    {
                        return MemberResponseDTO.toDTO(member, null);
                    }
                    return MemberResponseDTO.toDTOWithTimeTable(member, null);
                }).
                orElseThrow(()->new CustomException(ErrorCode.MEMBER_NOT_FOUND.getMessage(), ErrorCode.MEMBER_NOT_FOUND));
    }

}

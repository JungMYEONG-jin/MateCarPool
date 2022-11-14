package com.example.eunboard.service;

import com.example.eunboard.domain.dto.request.MemberRequestDTO;
import com.example.eunboard.domain.dto.response.MemberResponseDTO;
import com.example.eunboard.domain.entity.Member;
import com.example.eunboard.domain.repository.member.MemberRepository;

import com.example.eunboard.exception.ErrorCode;
import com.example.eunboard.exception.custom.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

import org.apache.catalina.security.SecurityUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

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

    public MemberResponseDTO select(final Long id) {
        return MemberResponseDTO.toDTO(memberRepository.findById(id).get(), null);
    }

    public void updateMember(final Long memberId, final MemberRequestDTO requestDTO) {
        if (null == requestDTO) {
            throw new RuntimeException("Invalid arguments");
        }
        requestDTO.setMemberId(memberId);
        requestDTO.setMember(true);

        // 존재하지 않는 멤버일시 runtime exception
        Member member = memberRepository.findById(memberId).orElseThrow(()->new CustomException(ErrorCode.MEMBER_NOT_FOUND.getMessage(), ErrorCode.MEMBER_NOT_FOUND));

        copyNonNullProperties(requestDTO, member);

        memberRepository.save(member);
    }

    public void updateProfileImage(final Long memberId, final String fileName) {
        Member member = memberRepository.findById(memberId).get();
        member.setProfileImage(fileName);

        memberRepository.save(member);
    }

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

    public void updateMemberArea(final Long memberId, final MemberRequestDTO requestDTO) {
        if (null == requestDTO.getArea()) {
            throw new RuntimeException("Invalid arguments");
        }

        Member member = memberRepository.findById(memberId).get();
        member.setArea(requestDTO.getArea());

        memberRepository.save(member);
    }

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

    public MemberResponseDTO getMyInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null){
            throw new RuntimeException("인증 정보가 없습니다.");
        }
        // name is id
        long id = Long.parseLong(authentication.getName());
        Member member = memberRepository.findByMemberId(id);
        if (member==null)
        {
            throw new RuntimeException("ID와 매치되는 유저가 존재하지 않습니다.");
        }
        return MemberResponseDTO.toDTO(member, null);
    }


}

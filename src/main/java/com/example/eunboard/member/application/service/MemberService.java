package com.example.eunboard.member.application.service;

import com.example.eunboard.member.application.port.in.*;
import com.example.eunboard.member.application.port.out.MemberRepositoryPort;
import com.example.eunboard.member.domain.Member;

import com.example.eunboard.member.domain.MemberRole;
import com.example.eunboard.passenger.application.port.out.PassengerRepositoryPort;
import com.example.eunboard.passenger.domain.Passenger;
import com.example.eunboard.shared.exception.ErrorCode;
import com.example.eunboard.shared.exception.custom.CustomException;
import com.example.eunboard.shared.util.FileUploadUtils;
import com.example.eunboard.shared.util.MD5Generator;
import com.example.eunboard.ticket.application.port.out.TicketRepositoryPort;
import com.example.eunboard.ticket.domain.Ticket;
import com.example.eunboard.timetable.application.port.in.MemberTimetableRequestDTO;
import com.example.eunboard.timetable.application.port.out.MemberTimetableRepositoryPort;
import com.example.eunboard.timetable.domain.MemberTimetable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.beans.PropertyDescriptor;
import java.text.SimpleDateFormat;
import java.util.*;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static com.example.eunboard.shared.util.FileUploadUtils.*;

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
    private final PassengerRepositoryPort passengerRepositoryPort;
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
        return memberRepository.findById(id).map(member -> MemberResponseDTO.toDTO(member, null)).
                orElseThrow(()->new CustomException(ErrorCode.MEMBER_NOT_FOUND.getMessage(), ErrorCode.MEMBER_NOT_FOUND));
    }

    /**
     * 운전자 권한 체크
     * @param id
     * @return
     */
    @Override
    public boolean checkRole(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(()->new CustomException(ErrorCode.MEMBER_NOT_FOUND.getMessage(), ErrorCode.MEMBER_NOT_FOUND))
                .getAuth().equals(MemberRole.DRIVER);
    }

    @Override
    public void checkMember(Long id) {
        memberRepository.findById(id).orElseThrow(()->new CustomException(ErrorCode.MEMBER_NOT_FOUND.getMessage(), ErrorCode.MEMBER_NOT_FOUND));
    }

    @Override
    public void withdraw(Long id) {
        Member member = memberRepository.findById(id).get();
        member.setIsRemoved(1);
        member.setDeleteDate(new Date());
        memberRepository.save(member);
    }

    @Override
    public void updateMember(Long memberId, MultipartFile multipartFile, MemberUpdateRequestDTO requestDTO) {
        Member member = memberRepository.findById(memberId).get();
        // 이미지 존재시
        if (multipartFile != null) {
            String originName = multipartFile.getOriginalFilename();
            String ext = originName.substring(originName.lastIndexOf(".") + 1); // 확장자
            String newFileName = new MD5Generator(originName).toString() + "." + ext; // 파일 해쉬
            cleanDir("/image/profiles/"+memberId);
            saveFile("/image/profiles/" + memberId, newFileName, multipartFile);
            member.setProfileImage("/" + memberId + "/" + newFileName);
        }
        // 변경 사항 체크
        if (requestDTO!=null){
            if (requestDTO.getAuth()!=null){
                if (!requestDTO.getAuth().equals(member.getAuth()))
                    member.setAuth(requestDTO.getAuth()); // update
            }
            if (requestDTO.getMemberTimeTable()!=null){
                    memberTimetableRepositoryPort.deleteAllInBatch(memberTimetableRepositoryPort.findByMember(Member.builder().memberId(memberId).build()));
                    List<MemberTimetableRequestDTO> memberTimeTable = requestDTO.getMemberTimeTable();
                    List<MemberTimetable> timetableEntities = new ArrayList<>();
                    memberTimeTable.forEach(timeTable -> {
                        timeTable.setMemberId(memberId);
                        timetableEntities.add(MemberTimetableRequestDTO.toEntity(timeTable));
                    });
                    memberTimetableRepositoryPort.saveAll(timetableEntities);
            }
        }
        memberRepository.save(member);
    }

    @Override
    public MemberUpdateResponseDTO getUpdateView(Long memberId) {
        Member member = memberRepository.findById(memberId).get();
        return MemberUpdateResponseDTO.of(member);
    }

    /**
     * jmj
     * 권한 없으면 403, 정보 없으면 404 return
     * @return
     */
    @Override
    public ProfileResponseDto getMyInfo(Long memberId){
        Member member = memberRepository.findById(memberId).get(); // controller 에서 존재 확인 했음.
        List<Passenger> boardingList = passengerRepositoryPort.getBoardingList(memberId);
        List<Ticket> tickets = new ArrayList<>();
        if (boardingList!=null)
        {
            for (Passenger passenger : boardingList) {
                tickets.add(passenger.getTicket());
            }
        }
        return ProfileResponseDto.of(member, tickets);
    }

}

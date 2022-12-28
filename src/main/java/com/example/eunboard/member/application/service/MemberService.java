package com.example.eunboard.member.application.service;

import com.example.eunboard.member.application.port.in.*;
import com.example.eunboard.member.application.port.out.MemberRepositoryPort;
import com.example.eunboard.member.domain.Member;
import com.example.eunboard.member.domain.MemberRole;
import com.example.eunboard.passenger.application.port.out.PassengerRepositoryPort;
import com.example.eunboard.passenger.domain.Passenger;
import com.example.eunboard.shared.exception.ErrorCode;
import com.example.eunboard.shared.exception.custom.CustomException;
import com.example.eunboard.shared.util.MD5Generator;
import com.example.eunboard.ticket.application.port.out.TicketRepositoryPort;
import com.example.eunboard.ticket.domain.Ticket;
import com.example.eunboard.timetable.application.port.in.MemberTimetableRequestDTO;
import com.example.eunboard.timetable.application.port.out.MemberTimetableRepositoryPort;
import com.example.eunboard.timetable.domain.MemberTimetable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.eunboard.shared.util.FileUploadUtils.cleanDir;
import static com.example.eunboard.shared.util.FileUploadUtils.saveFile;

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
    private final TicketRepositoryPort ticketRepositoryPort;

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
    public void checkStudentNumber(String studentNumber) {
        // studentNumber check
        if (memberRepository.existsByStudentNumber(studentNumber)) {
            throw new CustomException(ErrorCode.STUDENT_NUM_EXIST.getMessage(), ErrorCode.STUDENT_NUM_EXIST);
        }
    }

    @Override
    public void checkPhoneNumber(String phoneNumber) {
        // phone check
        if (memberRepository.existsByPhoneNumber(phoneNumber)){
            throw new CustomException(ErrorCode.PHONE_IS_EXIST.getMessage(), ErrorCode.PHONE_IS_EXIST);
        }
    }

    @Override
    public void updateMember(Long memberId, MemberUpdateRequestDTO requestDTO) {
        Member member = memberRepository.findById(memberId).get();
        // 변경 사항 체크
        // 해당 번호가 존재하는데 자신의 번호가 아니라면 다른 사람 번호
        if (memberRepository.existsByPhoneNumber(requestDTO.getPhoneNumber()) && !member.getPhoneNumber().equals(requestDTO.getPhoneNumber())){
            throw  new CustomException(ErrorCode.PHONE_IS_EXIST.getMessage(), ErrorCode.PHONE_IS_EXIST);
        }

        if (!member.getPhoneNumber().equals(requestDTO.getPhoneNumber()))
            member.setPhoneNumber(requestDTO.getPhoneNumber());

        if (!requestDTO.getAuth().equals(member.getAuth()))
            member.setAuth(requestDTO.getAuth()); // update

        memberTimetableRepositoryPort.deleteAllInBatch(memberTimetableRepositoryPort.findByMember(Member.builder().memberId(memberId).build()));
        List<MemberTimetableRequestDTO> memberTimeTable = requestDTO.getMemberTimeTable();
        List<MemberTimetable> timetableEntities = new ArrayList<>();
        memberTimeTable.forEach(timeTable -> {
            timeTable.setMemberId(memberId);
            timetableEntities.add(MemberTimetableRequestDTO.toEntity(timeTable));
        });
        memberTimetableRepositoryPort.saveAll(timetableEntities);
        memberRepository.save(member);
    }

    @Override
    public void updateMemberProfileImage(Long memberId, MultipartFile multipartFile) {
        Member member = memberRepository.findById(memberId).get();
        // 이미지 존재시 save
        if (multipartFile != null) {
            String originName = multipartFile.getOriginalFilename();
            String ext = originName.substring(originName.lastIndexOf(".") + 1); // 확장자
            String newFileName = new MD5Generator(originName).toString() + "." + ext; // 파일 해쉬
            cleanDir("/image/profiles/"+memberId);
            saveFile("/image/profiles/" + memberId, newFileName, multipartFile);
            member.setProfileImage("/" + memberId + "/" + newFileName);
            memberRepository.save(member);
        }
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
    public ProfileResponseDto getMyInfoForPassenger(Long memberId){
        Member member = memberRepository.findById(memberId).get(); // controller 에서 존재 확인 했음.
        List<Ticket> tickets = passengerRepositoryPort.getBoardingList(memberId).stream().map(Passenger::getTicket).collect(Collectors.toList());
        return ProfileResponseDto.of(member, tickets);
    }

    @Override
    public ProfileResponseDto getMyInfoForDriver(Long memberId) {
        Member member = memberRepository.findById(memberId).get(); // controller 에서 존재 확인 했음.
        List<Ticket> tickets = ticketRepositoryPort.getRecentList(memberId);
        return ProfileResponseDto.of(member, tickets);
    }

}

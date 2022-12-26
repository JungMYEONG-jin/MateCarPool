package com.example.eunboard.member.adapter.in;

import com.example.eunboard.member.application.port.in.*;
import com.example.eunboard.timetable.application.port.in.MemberTimetableUseCase;
import com.example.eunboard.shared.util.FileUploadUtils;
import com.example.eunboard.shared.util.MD5Generator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/member")
@RestController
public class MemberController {

    private final MemberUseCase memberService;
    private final MemberTimetableUseCase memberTimetableService;

    @GetMapping
    public MemberResponseDTO selectMember(@AuthenticationPrincipal UserDetails userDetails) {
        Long memberId = Long.parseLong(userDetails.getUsername()); // member Id
        return memberService.select(memberId);
    }

    @ResponseBody
    @PostMapping("/area")
    public void updateMemberArea(@AuthenticationPrincipal UserDetails userDetails,
            @RequestBody MemberRequestDTO requestDTO) {
        Long memberId = Long.parseLong(userDetails.getUsername());
        memberService.updateMemberArea(memberId, requestDTO);
    }

    /**
     * US-11 프로필 업데이트
     * @param userDetails
     * @param multipartFile
     * @param requestDTO
     */
    @ResponseBody
    @PutMapping("/update")
    public ResponseEntity updateMember(@AuthenticationPrincipal UserDetails userDetails,
            @RequestPart(required = false, name = "image") MultipartFile multipartFile,
            @RequestPart(required = false, name = "userData") MemberUpdateRequestDTO requestDTO) {
        Long memberId = Long.parseLong(userDetails.getUsername());
        memberService.checkRole(memberId);
        memberService.updateMember(memberId, multipartFile, requestDTO);
        return ResponseEntity.ok("수정에 성공하였습니다.");
    }

    @GetMapping("profile/{id}/{imagename}")
    @ResponseBody
    public ResponseEntity<byte[]> getFile(@PathVariable("id") String id, @PathVariable("imagename") String imagename){	
        ResponseEntity<byte[]> result = null;
        try {
            File file = new File(System.getProperty("user.dir") + "/image/profiles/" +id+ "/" + imagename);
            HttpHeaders headers=new HttpHeaders();
            headers.add("Content-Type", Files.probeContentType(file.toPath()));
            result=new ResponseEntity<>(FileCopyUtils.copyToByteArray(file),headers,HttpStatus.OK );
        }catch (IOException e) {
            log.info("Could not file read : {}", e.getMessage());
        }
        return result;
    }

    /**
     * US-10 프로필 화면
     * @param userDetails
     * @return
     */
    @GetMapping("/me")
    public ResponseEntity<ProfileResponseDto> getMyInfo(@AuthenticationPrincipal UserDetails userDetails){
        long memberId = Long.parseLong(userDetails.getUsername());
        memberService.checkRole(memberId);
        return ResponseEntity.ok(memberService.getMyInfo(memberId));
    }

    @GetMapping("/{studentnum}")
    public ResponseEntity<MemberResponseDTO> getMemberInfo(@PathVariable String studentnum){
        return ResponseEntity.ok(memberService.getMember(studentnum));
    }
}

package com.example.eunboard.member.adapter.in;

import com.example.eunboard.member.application.port.in.MemberRequestDTO;
import com.example.eunboard.member.application.port.in.MemberResponseDTO;
import com.example.eunboard.member.application.port.in.MemberUseCase;
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
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/member")
@RestController
public class MemberController {

    private final MemberUseCase memberService;

    private final MemberTimetableUseCase memberTimetableService;

    @GetMapping
    public MemberResponseDTO selectMember(@AuthenticationPrincipal long memberId) {
        return memberService.select(memberId);
    }

    @ResponseBody
    @PostMapping("/area")
    public void updateMemberArea(@AuthenticationPrincipal long memberId,
            @RequestBody MemberRequestDTO requestDTO) {
        memberService.updateMemberArea(memberId, requestDTO);
    }

    @ResponseBody
    @PostMapping("/new")
    public void updateMember(@AuthenticationPrincipal long memberId,
            @RequestPart(required = false, name = "image") MultipartFile multipartFile,
            @RequestPart(required = false, name = "userData") MemberRequestDTO requestDTO) {

        // FileUploadUtils.cleanDir("/image/profiles");
        if (multipartFile != null) {
            String originName = multipartFile.getOriginalFilename();
            String ext = originName.substring(originName.lastIndexOf(".") + 1); // 확장자

            String newFileName = new MD5Generator(originName).toString() + "." + ext; // 파일 해쉬
            FileUploadUtils.saveFile("/image/profiles/" + memberId, newFileName, multipartFile);

            memberService.updateProfileImage(memberId, "/" + memberId + "/" + newFileName);
        }

        memberTimetableService.saveAll(memberId, requestDTO.getMemberTimeTable());
        memberService.updateMember(memberId, requestDTO);
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

    @GetMapping("/me")
    public ResponseEntity<MemberResponseDTO> getMyInfo(){
        return ResponseEntity.ok(memberService.getMyInfo());
    }

    @GetMapping("/{studentnum}")
    public ResponseEntity<MemberResponseDTO> getMemberInfo(@PathVariable String studentnum){
        return ResponseEntity.ok(memberService.getMember(studentnum));
    }
}

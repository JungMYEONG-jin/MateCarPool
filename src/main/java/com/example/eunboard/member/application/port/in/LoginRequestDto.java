package com.example.eunboard.member.application.port.in;

import com.example.eunboard.shared.validation.stdnum.StudentNumUnique;
import lombok.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class LoginRequestDto {

    @NotBlank
    @Pattern(regexp = "[a-zA-Z0-9]+", message = "학번은 숫자, 영어만 입력 가능합니다.")
    /** 비밀번호 */
    private String password;

    /** 이름 */
    @Pattern(regexp = "[가-힣]+", message = "이름은 한글만 입력 가능합니다.")
    @Size(max = 4, message = "이름은 최대 4자까지 가능합니다.")
    @NotBlank
    private String memberName;

    /** 연락처 */
    private String phoneNumber;

    public UsernamePasswordAuthenticationToken toAuthentication(){
        return new UsernamePasswordAuthenticationToken(phoneNumber, password);
    }
}

package com.example.eunboard.validation.stdnum;

import com.example.eunboard.domain.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.MessageFormat;

@Component
public class StudentNumDuplicationValidator implements ConstraintValidator<StudentNumUnique, String> {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public void initialize(StudentNumUnique constraintAnnotation) {
    }

    @Override
    public boolean isValid(String studentNumber, ConstraintValidatorContext context) {
        boolean isExist = memberRepository.existsByStudentNumber(studentNumber);
        if (isExist){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(MessageFormat.format("{0} 은 이미 등록된 학번 또는 사번입니다.", studentNumber))
                    .addConstraintViolation();
        }
        return !isExist;
    }
}

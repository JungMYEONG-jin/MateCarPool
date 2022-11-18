package com.example.eunboard.shared.validation.stdnum;

import com.example.eunboard.member.adapter.out.repository.MemberRepository;
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
        }
        return !isExist;
    }
}

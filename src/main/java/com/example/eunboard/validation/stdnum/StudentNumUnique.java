package com.example.eunboard.validation.stdnum;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = StudentNumDuplicationValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface StudentNumUnique {
    String message() default "중복된 학번입니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

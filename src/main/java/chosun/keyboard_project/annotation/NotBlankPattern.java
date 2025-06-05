package chosun.keyboard_project.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NotBlankPatternValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface NotBlankPattern {

    String message() default "형식이 올바르지 않습니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String pattern();
    String blankMessage() default "값은 필수입니다.";
    String patternMessage() default "형식이 올바르지 않습니다.";
}
package chosun.keyboard_project.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotBlankPatternValidator implements ConstraintValidator<NotBlankPattern, String> {

    private String pattern;
    private String blankMessage;
    private String patternMessage;

    @Override
    public void initialize(NotBlankPattern constraintAnnotation) {
        this.pattern = constraintAnnotation.pattern();
        this.blankMessage = constraintAnnotation.blankMessage();
        this.patternMessage = constraintAnnotation.patternMessage();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(blankMessage).addConstraintViolation();
            return false;
        }

        if (!value.matches(pattern)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(patternMessage).addConstraintViolation();
            return false;
        }

        return true;
    }
}

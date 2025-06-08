package chosun.keyboard_project.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotBlankPatternValidator implements ConstraintValidator<NotBlankPattern, String> {

    private String pattern;
    private String blankMessage;
    private String patternMessage;
    private String propertyName;

    @Override
    public void initialize(NotBlankPattern constraintAnnotation) {
        this.pattern = constraintAnnotation.pattern();
        this.blankMessage = constraintAnnotation.blankMessage();
        this.patternMessage = constraintAnnotation.patternMessage();
        this.propertyName = constraintAnnotation.propertyName(); //  필드명 저장
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate(blankMessage)
                    .addPropertyNode(propertyName) //  필드에 바인딩
                    .addConstraintViolation();
            return false;
        }

        if (!value.matches(pattern)) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate(patternMessage)
                    .addPropertyNode(propertyName)
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}

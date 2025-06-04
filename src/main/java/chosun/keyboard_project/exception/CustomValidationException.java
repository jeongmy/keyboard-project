package chosun.keyboard_project.exception;

import java.util.Map;

public class CustomValidationException extends RuntimeException {

    private Map<String, String > errors;

    public CustomValidationException(Map<String, String> errors) {
        super("유효성 검사 실패");
        this.errors = errors;
    }

    public Map<String, String> getErrors(){
        return errors;
    }

}

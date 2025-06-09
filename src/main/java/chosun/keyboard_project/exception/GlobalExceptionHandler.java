package chosun.keyboard_project.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateUsernameException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateUsername(DuplicateUsernameException e){
        Map<String, Object> body = new HashMap<>();

        body.put("code", 409);
        body.put("error", "Duplicate Username");
        body.put("message", e.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);

    }

    @ExceptionHandler(LoginFailException.class)
    public ResponseEntity<Map<String, Object>> handleLoginFailed(LoginFailException e) {
        Map<String, Object> body = new HashMap<>();

        body.put("code", 401); // Unauthorized
        body.put("error", "Login Failed");
        body.put("message", e.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(EntityNotFoundException e){
        Map<String, Object> body = new HashMap<>();

        body.put("code", 404);
        body.put("error", "NotFound");
        body.put("message", e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(body);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequset(RuntimeException e){
        Map<String, Object> body = new HashMap<>();

        body.put("code",400);
        body.put("error", "BadRequest");
        body.put("message", e.getMessage());

        System.out.println(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException e){
//        Map<String, Object> body = new HashMap<>();
//        body.put("error", "Validation Failed");
//        body.put("message", e.getMessage());
//        body.put("code", 400);
//        System.out.println(e.getMessage());
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage()); // <-- 여기서 NotBlank의 message 추출 가능
        });

        Map<String, Object> body = new HashMap<>();
        body.put("code", 400);
        body.put("error", "Validation Failed");
        body.put("message", errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);

    }

    @ExceptionHandler(CustomValidationException.class)
    public ResponseEntity<Map<String, Object>> handleCustomValidation(CustomValidationException e) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", "Validation Failed");
        body.put("message", e.getErrors());
        body.put("code", 400);
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException e) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", "Bad Request");
        body.put("message", e.getMessage());
        body.put("code", 400);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }


}

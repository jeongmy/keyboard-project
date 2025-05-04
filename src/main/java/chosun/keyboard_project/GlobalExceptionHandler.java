package chosun.keyboard_project;

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

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(EntityNotFoundException e){
        Map<String, Object> body = new HashMap<>();
        body.put("error", "NotFound");
        body.put("message", e.getMessage());
        body.put("code", 404);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(body);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequset(RuntimeException e){
        Map<String, Object> body = new HashMap<>();
        body.put("error", "BadRequest");
        body.put("message", e.getMessage());
        body.put("code",400);
        System.out.println(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException e){
        Map<String, Object> body = new HashMap<>();
        body.put("error", "Validation Failed");
        body.put("message", e.getMessage());
        body.put("code", 400);
        System.out.println("회원가입 실패(유효성 검사 실패)");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
}

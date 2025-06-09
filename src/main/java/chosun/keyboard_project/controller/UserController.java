package chosun.keyboard_project.controller;


import chosun.keyboard_project.domain.User;
import chosun.keyboard_project.dto.userDTO.*;
import chosun.keyboard_project.exception.DuplicateUsernameException;
import chosun.keyboard_project.repository.UserRepository;
import chosun.keyboard_project.service.CustomUserDetails;
import chosun.keyboard_project.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/users")
//@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/join")
    public ResponseEntity<?> join(@Valid @RequestBody UserJoinRequestDTO dto, BindingResult bindingResult) {
        System.out.println("회원가입 요청 들어옴: " + dto.getUsername());

        Map<String, String> errors = new HashMap<>();

        if (bindingResult.hasErrors()) {
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
        }

        if (userRepository.findByUserId(dto.getUserId()).isPresent()) {
            errors.put("userId", "이미 존재하는 ID입니다.");
        }

        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            errors.put("email", "이미 등록된 이메일입니다.");
        }

        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            errors.put("username", "이미 사용 중인 닉네임입니다.");
        }

        if (!dto.getPassword().equals(dto.getPasswordConfirm())) {
            errors.put("passwordConfirm", "비밀번호가 일치하지 않습니다.");
        }

        // 오류 하나라도 있으면 응답 반환
        if (!errors.isEmpty()) {
            Map<String, Object> body = new HashMap<>();
            body.put("code", 400);
            body.put("error", "Validation Failed");
            body.put("message", errors); // 여기에 field error map

            return ResponseEntity.badRequest().body(body);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.join(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDTO> login(@Valid @RequestBody UserLoginRequestDTO dto) {
        System.out.println("로그인 요청 들어옴: " + dto.getUserId());
        return ResponseEntity.ok(userService.login(dto));
    }

    @GetMapping("/me")
    public ResponseEntity<UserMeResponseDTO> getMyInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();

        UserMeResponseDTO response = new UserMeResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getUserId(),
                user.getEmail(),
                user.getRole()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/me")
    public ResponseEntity<String> deleteUser(
            @RequestBody PasswordCheckDTO dto, @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (!passwordEncoder.matches(dto.getUserPassword(), userDetails.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호가 일치하지 않습니다.");
        }

        userRepository.deleteById(userDetails.getUser().getId());
        return ResponseEntity.ok("회원탈퇴 완료.");

    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changeUserPasswrod(
                @RequestBody PasswordChangeRequestDTO dto,
                @AuthenticationPrincipal CustomUserDetails userDetails
            ){
        if(!passwordEncoder.matches(dto.getCurrentPassword(), userDetails.getPassword())){
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        if(!dto.getNewPassword().equals(dto.getNewPasswordConfirm())){
            throw new IllegalArgumentException("새 비밀번호가 일치하지 않습니다.");
        }

        User user = userDetails.getUser();
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);

        return ResponseEntity.ok("비밀번호 변경 완료");

    }

    @PostMapping("/check-username") // 사용가능하면 True 아니면 False 반환
    public ResponseEntity<Boolean> checkUsername(@Valid @RequestBody UsernameCheckRequestDTO dto){
        return ResponseEntity.ok(!userRepository.findByUsername(dto.getUsername()).isPresent());
    }

    @PostMapping("/check-id")
    public ResponseEntity<Boolean> checkUserId(@Valid @RequestBody UserIdCheckRequestDTO dto){
        return ResponseEntity.ok(!userRepository.findByUserId(dto.getUserId()).isPresent());

    }

    @PostMapping("/check-email")
    public ResponseEntity<Boolean> checkEmail(@RequestParam("userEmail") String userEmail){
        return ResponseEntity.ok(!userRepository.findByEmail(userEmail).isPresent());
    }

    @PostMapping("/find-id")
    public ResponseEntity<String> findUserId(@RequestBody UserEmailDTO dto){
        User user = userRepository.findByEmail(dto.getUserEmail())
                .orElseThrow(() -> new EntityNotFoundException("이메일과 관련된 ID가 존재하지 않습니다."));
        return ResponseEntity.ok(user.getUserId());
    }

    @PostMapping("/reset-password") // 비밀번호 찾기 시 이메일로 임시 비밀번호 보냄
    public ResponseEntity<String> resetPassword(@RequestBody UserEmailDTO dto){
        System.out.println("임시 비밀번호 요청 들어옴");
        userService.resetPassword(dto.getUserEmail());
        return ResponseEntity.ok("임시 비밀번호가 이메일로 전송되었습니다.");
    }

}

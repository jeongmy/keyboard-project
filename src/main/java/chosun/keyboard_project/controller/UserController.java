package chosun.keyboard_project.controller;


import chosun.keyboard_project.domain.User;
import chosun.keyboard_project.dto.*;
import chosun.keyboard_project.service.CustomUserDetails;
import chosun.keyboard_project.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
//@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/join")
    public ResponseEntity<UserJoinResponseDTO> join(@Valid @RequestBody UserJoinRequestDTO dto) {
        System.out.println("회원가입 요청 들어옴: " + dto.getUsername());
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


}

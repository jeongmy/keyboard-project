package chosun.keyboard_project.controller;


import chosun.keyboard_project.dto.UserJoinRequestDTO;
import chosun.keyboard_project.dto.UserJoinResponseDTO;
import chosun.keyboard_project.dto.UserLoginRequestDTO;
import chosun.keyboard_project.dto.UserLoginResponseDTO;
import chosun.keyboard_project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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
    public UserJoinResponseDTO join(@RequestBody UserJoinRequestDTO dto) {
        System.out.println("회원가입 요청 들어옴: " + dto.getUsername());
        return userService.join(dto);
    }

    @PostMapping("/login")
    public UserLoginResponseDTO login(@RequestBody UserLoginRequestDTO dto) {
        return userService.login(dto);
    }

}

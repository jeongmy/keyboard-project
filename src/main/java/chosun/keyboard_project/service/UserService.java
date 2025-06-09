package chosun.keyboard_project.service;

import chosun.keyboard_project.JwtTokenProvider;
import chosun.keyboard_project.domain.User;
import chosun.keyboard_project.dto.userDTO.UserJoinRequestDTO;
import chosun.keyboard_project.dto.userDTO.UserJoinResponseDTO;
import chosun.keyboard_project.dto.userDTO.UserLoginRequestDTO;
import chosun.keyboard_project.dto.userDTO.UserLoginResponseDTO;
import chosun.keyboard_project.exception.DuplicateUsernameException;
import chosun.keyboard_project.exception.LoginFailException;
import chosun.keyboard_project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final MailService mailService;


    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       JwtTokenProvider jwtTokenProvider, MailService mailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.mailService = mailService;
    }

    public UserJoinResponseDTO join(UserJoinRequestDTO dto) {

        User user = User.builder()
                .username(dto.getUsername())
                .userId(dto.getUserId())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role("ROLE_USER")
                .build();

        User saved = userRepository.save(user);
        System.out.println(dto.getUsername()+"님 회원가입 완료");
        return new UserJoinResponseDTO(
                saved.getId(),
                saved.getUsername(),
                saved.getUserId(),
                saved.getEmail(),
                saved.getRole()
        );

    }

    public UserLoginResponseDTO login(UserLoginRequestDTO dto) {
        User user = userRepository.findByUserId(dto.getUserId())
                .orElseThrow(() -> new LoginFailException("사용자를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            System.out.println(user.getUserId() + ":로그인 실패");
            throw new LoginFailException("ID:"+ dto.getUserId() +" 비밀번호가 일치하지 않습니다.");
        }

        String token = jwtTokenProvider.createToken(user.getUsername(), user.getRole());
        System.out.println(dto.getUserId()+"님 로그인 성공");
        return new UserLoginResponseDTO(token);
    }

    public void resetPassword(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new IllegalArgumentException("등록되지 않은 이메일입니다."));
        String tempPassword = UUID.randomUUID().toString().substring(0,8);
        user.setPassword(passwordEncoder.encode(tempPassword));
        userRepository.save(user);

        mailService.sendTemporaryMessage(email, tempPassword);
    }

}

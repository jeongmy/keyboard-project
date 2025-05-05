package chosun.keyboard_project.service;

import chosun.keyboard_project.JwtTokenProvider;
import chosun.keyboard_project.domain.User;
import chosun.keyboard_project.dto.UserJoinRequestDTO;
import chosun.keyboard_project.dto.UserJoinResponseDTO;
import chosun.keyboard_project.dto.UserLoginRequestDTO;
import chosun.keyboard_project.dto.UserLoginResponseDTO;
import chosun.keyboard_project.exception.DuplicateUsernameException;
import chosun.keyboard_project.exception.LoginFailException;
import chosun.keyboard_project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;


    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public UserJoinResponseDTO join(UserJoinRequestDTO dto) {
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new DuplicateUsernameException(dto.getUsername()+"는 이미 존재하는 ID입니다.");
        }

        User user = User.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role("ROLE_USER")
                .build();

        User saved = userRepository.save(user);
        System.out.println(dto.getUsername()+"님 회원가입 완료");
        return new UserJoinResponseDTO(saved.getId(), saved.getUsername(), saved.getRole());
    }

    public UserLoginResponseDTO login(UserLoginRequestDTO dto) {
        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new LoginFailException("사용자를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new LoginFailException("ID: "+ dto.getUsername() +", 비밀번호가 일치하지 않습니다.");
        }

        String token = jwtTokenProvider.createToken(user.getUsername(), user.getRole());
        System.out.println(dto.getUsername()+"님 로그인 성공");
        return new UserLoginResponseDTO(token);
    }


}

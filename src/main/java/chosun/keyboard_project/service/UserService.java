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
        // userId 중복 체크
        if (userRepository.findByUserId(dto.getUserId()).isPresent()) {
            System.out.println(dto.getUserId() + "는 이미 존재하는 ID: 회원가입 실패");
            throw new DuplicateUsernameException(dto.getUserId() + "는 이미 존재하는 ID입니다.");
        }

        // 이메일 중복 체크
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            System.out.println(dto.getEmail() + "는 이미 등록된 이메일입니다.");
            throw new DuplicateUsernameException(dto.getEmail() + "는 이미 등록된 이메일입니다.");
        }

        // 닉네임 중복 체크 (필요 시)
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            System.out.println(dto.getUsername() + "는 이미 사용 중인 닉네임입니다.");
            throw new DuplicateUsernameException(dto.getUsername() + "는 이미 사용 중인 닉네임입니다.");
        }

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
            throw new LoginFailException("ID: "+ dto.getUserId() +", 비밀번호가 일치하지 않습니다.");
        }

        String token = jwtTokenProvider.createToken(user.getUsername(), user.getRole());
        System.out.println(dto.getUserId()+"님 로그인 성공");
        return new UserLoginResponseDTO(token);
    }


}

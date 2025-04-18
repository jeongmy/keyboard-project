package chosun.keyboard_project.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 자동 증가하는 기본 키

    @Column(nullable = false, unique = true)
    private String username; // 아이디: (필수, 중복 불가)

    @Column(nullable = false)
    private String password; // 비밀번호: (필수)

    private String role; // 사용자 역할(예 ROLE_USER, ROLE, ADMIN)

}

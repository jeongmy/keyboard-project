package chosun.keyboard_project.repository;


import chosun.keyboard_project.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(String userId);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username); // 닉네임 중복 검사
}

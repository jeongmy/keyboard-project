package chosun.keyboard_project.repository;

import chosun.keyboard_project.domain.Favorite;
import chosun.keyboard_project.domain.Keyboard;
import chosun.keyboard_project.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    Optional<Favorite> findByUserAndKeyboard(User user, Keyboard keyboard);

    List<Favorite> findAllByUser(User user);

    void deleteByUserAndKeyboard(User user, Keyboard keyboard);
}
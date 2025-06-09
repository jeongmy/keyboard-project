package chosun.keyboard_project.repository;

import chosun.keyboard_project.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c JOIN FETCH c.user WHERE c.keyboard.id = :keyboardId ORDER BY c.createdAt DESC")
    List<Comment> findByKeyboardIdWithUser(@Param("keyboardId") Long keyboardId);
}

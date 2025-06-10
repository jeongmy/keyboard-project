package chosun.keyboard_project.service;

import chosun.keyboard_project.domain.Comment;
import chosun.keyboard_project.domain.Keyboard;
import chosun.keyboard_project.domain.User;
import chosun.keyboard_project.dto.CommentRequestDTO;
import chosun.keyboard_project.dto.CommentResponseDTO;
import chosun.keyboard_project.repository.CommentRepository;
import chosun.keyboard_project.repository.KeyboardRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final KeyboardRepository keyboardRepository;
    private final CommentRepository commentRepository;

    public CommentService(KeyboardRepository keyboardRepository, CommentRepository commentRepository){
        this.keyboardRepository = keyboardRepository;
        this.commentRepository = commentRepository;
    }

    @Transactional
    public void writeComment(CommentRequestDTO dto, User user) {
        Keyboard keyboard = keyboardRepository.findById(dto.getKeyboardId())
                .orElseThrow(() -> new EntityNotFoundException("키보드를 찾을 수 없습니다."));

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setKeyboard(keyboard);
        comment.setContent(dto.getContent());

        commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponseDTO> getCommentsByKeyboard(Long keyboardId) {
        List<Comment> comments = commentRepository.findByKeyboardIdWithUser(keyboardId);

        return comments.stream()
                .map(c -> new CommentResponseDTO(
                        c.getId(),
                        c.getUser().getUsername(),
                        c.getContent(),
                        c.getCreatedAt()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteComment(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));

        if (!comment.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "자신의 댓글만 삭제할 수 있습니다.");
        }

        commentRepository.delete(comment);
    }

}

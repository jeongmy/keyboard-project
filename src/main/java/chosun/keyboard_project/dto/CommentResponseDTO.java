package chosun.keyboard_project.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CommentResponseDTO {
    private Long commentId;
    private String username;
    private String content;
    private LocalDateTime createdAt;

    public CommentResponseDTO(Long commentId, String username, String content, LocalDateTime createdAt) {
        this.commentId = commentId;
        this.username = username;
        this.content = content;
        this.createdAt = createdAt;
    }
}
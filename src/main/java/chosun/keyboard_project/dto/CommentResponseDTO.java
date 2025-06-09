package chosun.keyboard_project.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CommentResponseDTO {
    private String username;
    private String content;
    private LocalDateTime createdAt;

    public CommentResponseDTO(String username, String content, LocalDateTime createdAt) {
        this.username = username;
        this.content = content;
        this.createdAt = createdAt;
    }
}
package chosun.keyboard_project.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CommentResponseDTO {
    private String nickname;
    private String content;
    private LocalDateTime createdAt;

    public CommentResponseDTO(String nickname, String content, LocalDateTime createdAt) {
        this.nickname = nickname;
        this.content = content;
        this.createdAt = createdAt;
    }
}
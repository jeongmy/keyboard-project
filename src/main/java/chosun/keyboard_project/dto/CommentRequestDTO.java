package chosun.keyboard_project.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentRequestDTO {
    private Long keyboardId;

    @NotBlank(message = "댓글 내용을 입력해주세요.")
    private String content;

}

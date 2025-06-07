package chosun.keyboard_project.dto.userDTO;

import chosun.keyboard_project.annotation.NotBlankPattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UsernameCheckRequestDTO {
    @NotBlankPattern(
            pattern = "^[가-힣a-zA-Z0-9._]{2,20}$",
            blankMessage = "닉네임은 필수입니다.",
            patternMessage = "닉네임은 한글, 영문, 숫자, ., _만 사용 가능하며 공백 없이 2~20자여야 합니다."
    )
    private String username;
}

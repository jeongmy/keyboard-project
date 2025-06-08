package chosun.keyboard_project.dto.userDTO;

import chosun.keyboard_project.annotation.NotBlankPattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserIdCheckRequestDTO {

    @NotBlankPattern(
            pattern = "^[a-z0-9]{4,16}$",
            blankMessage = "아이디는 필수입니다.",
            patternMessage = "아이디는 소문자와 숫자만 사용 가능하며 4~16자여야 합니다.",
            propertyName = "userId"

    )
    private String userId;
}

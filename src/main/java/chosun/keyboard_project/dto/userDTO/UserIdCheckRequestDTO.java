package chosun.keyboard_project.dto.userDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserIdCheckRequestDTO {

    @NotBlank(message = "소문자와 숫자만 사용하여 4~16자 내의 아이디를 입력해 주세요.")
    @Pattern(
            regexp = "^[a-z0-9]{4,16}$",
            message = "소문자와 숫자만 사용하여 4~16자 내의 아이디를 입력해 주세요."
    )
    private String userId;
}

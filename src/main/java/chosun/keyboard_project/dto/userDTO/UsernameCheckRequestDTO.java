package chosun.keyboard_project.dto.userDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UsernameCheckRequestDTO {
    @NotBlank(message = "한글, 영문, 숫자, ., _을 사용하여 공백 없이 2~20자 내로 닉네임을 입력해 주세요.")
    @Pattern(
            regexp = "^[가-힣a-zA-Z0-9._]{2,20}$",
            message = "한글, 영문, 숫자, ., _을 사용하여 공백 없이 2~20자 내로 닉네임을 입력해 주세요."
    )
    private String username;
}

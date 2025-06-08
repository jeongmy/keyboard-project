package chosun.keyboard_project.dto.userDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserJoinRequestDTO {

    @NotBlank(message = "한글, 영문, 숫자, ., _을 사용하여 공백 없이 2~20자 내로 닉네임을 입력해 주세요.")
    @Pattern(
            regexp = "^[가-힣a-zA-Z0-9._]{2,20}$",
            message = "한글, 영문, 숫자, ., _을 사용하여 공백 없이 2~20자 내로 닉네임을 입력해 주세요."
    )
    private String username; // 닉네임

    @NotBlank(message = "소문자와 숫자만 사용하여 4~16자 내의 아이디를 입력해 주세요.")
    @Pattern(
            regexp = "^[a-z0-9]{4,16}$",
            message = "소문자와 숫자만 사용하여 4~16자 내의 아이디를 입력해 주세요."
    )
    private String userId; // 로그인용 아이디

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

    @NotBlank(message = "비밀번호 확인")
    private String passwordConfirm;

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "올바른 이메일 형식이어야 합니다.")
    private String email;

}

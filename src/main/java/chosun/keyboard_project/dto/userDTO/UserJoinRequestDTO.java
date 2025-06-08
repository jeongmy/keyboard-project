package chosun.keyboard_project.dto.userDTO;

import chosun.keyboard_project.annotation.NotBlankPattern;
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

    @NotBlankPattern(
            pattern = "^[가-힣a-zA-Z0-9._]{2,20}$",
            blankMessage = "닉네임은 필수입니다.",
            patternMessage = "닉네임은 한글, 영문, 숫자, ., _만 사용 가능하며 공백 없이 2~20자여야 합니다.",
            propertyName = "username" //  필드 이름 명시
    )
    private String username; // 닉네임

    @NotBlankPattern(
            pattern = "^[a-z0-9]{4,16}$",
            blankMessage = "아이디는 필수입니다.",
            patternMessage = "아이디는 소문자와 숫자만 사용 가능하며 4~16자여야 합니다.",
            propertyName = "userId"
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

package chosun.keyboard_project.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserJoinRequestDTO {
//    @NotBlank(message = "아이디는 필수입니다.")
//    private String username;
//
//    @NotBlank(message = "비밀번호는 필수입니다.")
//    private String password;
//
    @NotBlank(message = "닉네임은 필수입니다.")
    private String username; // 닉네임

    @NotBlank(message = "아이디는 필수입니다.")
    private String userId; // 로그인용 아이디

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "올바른 이메일 형식이어야 합니다.")
    private String email;


}

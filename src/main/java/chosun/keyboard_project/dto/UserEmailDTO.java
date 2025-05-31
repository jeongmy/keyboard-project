package chosun.keyboard_project.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserEmailDTO {
    @Email(message = "올바른 이메일 형식이여야 합니다.")
    @NotBlank(message = "이메일은 필수입니다.")
    private String userEmail;
}

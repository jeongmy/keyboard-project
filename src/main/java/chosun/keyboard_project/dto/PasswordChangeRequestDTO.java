package chosun.keyboard_project.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PasswordChangeRequestDTO {

    @NotBlank(message = "현재 비밀번호")
    private String currentPassword;

    @NotBlank(message = "새 비밀번호")
    private String newPassword;

    @NotBlank(message = "새 비밀번호 확인")
    private String newPasswordConfirm;
}

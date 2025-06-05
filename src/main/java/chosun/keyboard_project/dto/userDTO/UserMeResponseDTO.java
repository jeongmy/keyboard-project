package chosun.keyboard_project.dto.userDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMeResponseDTO {
    private Long id;
    private String username; // 닉네임
    private String userId;
    private String email;
    private String role;
}

package chosun.keyboard_project.dto;

import chosun.keyboard_project.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserJoinResponseDTO {
//    private Long id;
//    private String username;
//    private String role;
    private Long id;
    private String username;   // 닉네임
    private String userId;     // 로그인용 아이디
    private String email;
    private String role;
}

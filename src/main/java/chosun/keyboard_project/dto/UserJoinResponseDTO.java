package chosun.keyboard_project.dto;

import chosun.keyboard_project.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserJoinResponseDTO {
    private Long id;
    private String username;
    private String role;


}

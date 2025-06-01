package chosun.keyboard_project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class SoundSetResponseDTO {
    private String Enter;
    private String BackSpace;
    private String LShift;
    private String RShift;
    private String SpaceBar;
    private String NormalKey;
}

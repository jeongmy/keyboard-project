package chosun.keyboard_project.dto.gptDTO;

import chosun.keyboard_project.dto.keyboardDTO.KeyboardFilterRequestDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MbtiGptResponseDTO {
    private KeyboardFilterRequestDTO filter;
    private String analysis;

}

package chosun.keyboard_project.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MbtiGptResponseDto {
    private KeyboardFilterRequestDto filter;
    private String analysis;

}

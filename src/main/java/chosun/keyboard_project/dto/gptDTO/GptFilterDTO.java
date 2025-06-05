package chosun.keyboard_project.dto.gptDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class GptFilterDTO {
    private List<String> weightLabels;
    private List<String> keyPressureLabels;
    private List<String> connections;
    private List<String> purposes;
    private List<String> layouts;
    private List<String> backlights;
    private List<String> switchTypes;
    private List<String> manufacturers;
    private List<String> priceRanges; // → ["50000~100000"] 이런 식으로 들어오게 유도할 것
}


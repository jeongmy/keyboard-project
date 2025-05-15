package chosun.keyboard_project.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class KeyboardFilterRequestDto {
    private List<PriceRangeDTO> priceRanges;
    private List<String> weightLabels;
    private List<String> keyPressureLabels;
    private List<String> connections;
    private List<String> purposes;
    private List<String> materials;
    private List<String> layouts;
    private List<String> backlights;
    private List<String> switchTypes;
    private List<String> manufacturers;
    private List<String> sounds;
}

package chosun.keyboard_project;

import chosun.keyboard_project.dto.GptFilterDto;
import chosun.keyboard_project.dto.KeyboardFilterRequestDto;
import chosun.keyboard_project.dto.PriceRangeParser;

public class GptMapper {

    public static KeyboardFilterRequestDto toKeyboardFilterDto(GptFilterDto gpt) {
        KeyboardFilterRequestDto dto = new KeyboardFilterRequestDto();

        dto.setWeightLabels(gpt.getWeightLabels());
        dto.setKeyPressureLabels(gpt.getKeyPressureLabels());
        dto.setConnections(gpt.getConnections());
        dto.setPurposes(gpt.getPurposes());
        dto.setLayouts(gpt.getLayouts());
        dto.setBacklights(gpt.getBacklights());
        dto.setSwitchTypes(gpt.getSwitchTypes());
        dto.setManufacturers(gpt.getManufacturers());

        dto.setPriceRanges(PriceRangeParser.parse(gpt.getPriceRanges()));

        return dto;
    }
}

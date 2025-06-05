package chosun.keyboard_project.gpt_utill;

import chosun.keyboard_project.dto.gptDTO.GptFilterDTO;
import chosun.keyboard_project.dto.keyboardDTO.KeyboardFilterRequestDTO;
import chosun.keyboard_project.dto.PriceRangeParser;

public class GptMapper {

    public static KeyboardFilterRequestDTO toKeyboardFilterDto(GptFilterDTO gpt) {
        KeyboardFilterRequestDTO dto = new KeyboardFilterRequestDTO();

        dto.setWeightLabels(gpt.getWeightLabels());
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

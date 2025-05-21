package chosun.keyboard_project.dto;

import java.util.ArrayList;
import java.util.List;

public class PriceRangeParser {

    public static List<PriceRangeDTO> parse(List<String> priceRangesRaw) {
        List<PriceRangeDTO> result = new ArrayList<>();
        if (priceRangesRaw == null) return result;

        for (String range : priceRangesRaw) {
            String[] parts = range.split("~");
            PriceRangeDTO dto = new PriceRangeDTO();

            // 앞 또는 뒤가 null일 수 있음
            if (!parts[0].equalsIgnoreCase("null")) {
                dto.setMin(Integer.parseInt(parts[0]));
            }
            if (!parts[1].equalsIgnoreCase("null")) {
                dto.setMax(Integer.parseInt(parts[1]));
            }

            result.add(dto);
        }

        return result;
    }
}

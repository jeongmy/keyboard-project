package chosun.keyboard_project.dto.keyboardDTO;

import chosun.keyboard_project.domain.Keyboard;
import chosun.keyboard_project.domain.KeyboardVariant;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
//@AllArgsConstructor 이거 있으면 밑에 모든 args있는 생성자 안 써도 됨. 대체 가능. 지금은 연습용으로 다 씀.
// -> 찜 기능으로 어쩔 수 없이 추가만 해놓음.
public class KeyboardDto {
    private Long id;
    private String name;
    private String manufacturer;
    private String layout;
    private String keyCount;
    private String connection;
    private String weightValue;
    private String weightLabel;
    private String backlight;
    private String housingColor;

    private List<KeyboardVariant> variant;

    private boolean liked = false; //  로그인한 사용자 기준 찜 여부

    // 생성자, getter, setter 생략 또는 Lombok 사용 가능

    /*
    @JsonPropertyOrder({
            "id", "name", "manufacturer", "switchType", "sound",
            "keyPressureValue", "keyPressureLabel", "layout", "material",
            "backlight", "weightValue", "weightLabel", "price"
    })*/
    public KeyboardDto(Long id, String name, String manufacturer, String layout
                        , String keyCount, String connection, String weightValue, String weightLabel
                        , String backlight, String housingColor
                        , List<KeyboardVariant> variant) {
        this.id = id;
        this.name = name;
        this.manufacturer = manufacturer;
        this.layout = layout;
        this.keyCount = keyCount;
        this.connection = connection;
        this.weightValue = weightValue;
        this.weightLabel = weightLabel;
        this.backlight = backlight;
        this.housingColor = housingColor;
        this.variant = variant;

    }

    public static KeyboardDto from(Keyboard keyboard, boolean liked) {
        KeyboardDto dto = new KeyboardDto();
        dto.setId(keyboard.getId());
        dto.setName(keyboard.getName());
        dto.setManufacturer(keyboard.getManufacturer());
        dto.setLayout(keyboard.getLayout());
        dto.setKeyCount(keyboard.getKeyCount());
        dto.setConnection(keyboard.getConnection());
        dto.setWeightValue(keyboard.getWeightValue());
        dto.setWeightLabel(keyboard.getWeightLabel());
        dto.setBacklight(keyboard.getBacklight());
        dto.setHousingColor(keyboard.getHousingColor());
        dto.setVariant(keyboard.getVariants()); // ← 연관된 스위치들
        dto.setLiked(liked); //  찜 여부 설정
        return dto;
    }
}

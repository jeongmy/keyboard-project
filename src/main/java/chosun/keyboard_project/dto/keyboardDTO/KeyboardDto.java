package chosun.keyboard_project.dto.keyboardDTO;

import chosun.keyboard_project.domain.KeyboardVariant;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
//@AllArgsConstructor 이거 있으면 밑에 모든 args있는 생성자 안 써도 됨. 대체 가능. 지금은 연습용으로 다 씀.
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
}

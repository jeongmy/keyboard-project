package chosun.keyboard_project.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
//@AllArgsConstructor 이거 있으면 밑에 모든 args있는 생성자 안 써도 됨. 대체 가능. 지금은 연습용으로 다 씀.
public class KeyboardDto {
    private Long id;
    private String name;
    private String manufacturer;
    private String switchType;
    private String sound;
    private int keyPressureValue;
    private String keyPressureLabel;
    private String layout;
    private String material;
    private String backlight;
    private float weightValue;
    private String weightLabel;
    private int price;
    private List<String> connections;  // connection label만 리스트로
    private List<String> purposes;     // purpose label만 리스트로

    // 생성자, getter, setter 생략 또는 Lombok 사용 가능

    /*
    @JsonPropertyOrder({
            "id", "name", "manufacturer", "switchType", "sound",
            "keyPressureValue", "keyPressureLabel", "layout", "material",
            "backlight", "weightValue", "weightLabel", "price"
    })*/
    public KeyboardDto(Long id, String name, String manufacturer, String switchType,
                       String sound, int keyPressureValue, String keyPressureLabel, String layout, String material,
                       String backlight, float weightValue, String weightLabel, int price,
                       List<String> connections, List<String> purposes) {
        this.id = id;
        this.name = name;
        this.manufacturer = manufacturer;
        this.switchType = switchType;
        this.sound = sound;
        this.keyPressureValue = keyPressureValue;
        this.keyPressureLabel = keyPressureLabel;
        this.layout = layout;
        this.material = material;
        this.backlight = backlight;
        this.weightValue = weightValue;
        this.weightLabel = weightLabel;
        this.price = price;
        this.connections = connections;
        this.purposes = purposes;
    }
}

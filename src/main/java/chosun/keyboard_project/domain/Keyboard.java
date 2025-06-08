package chosun.keyboard_project.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Keyboard {

    @Id
    private Long id;

    private String name;              // 키보드 이름
    private String manufacturer;      // 키보드 제조사
    private String layout;            // ex) 풀배열
    private String keyCount;          // ex) 108키
    private String connection;        // ex) 유선+무선
    private String weightValue;       // ex) 무게의 정확한 수치 900g 등
    private String weightLabel;       // ex) 가벼운
    private String backlight;         // 백라이트
    private String switchType;        // ex) 리니어
    private String housingColor;      // ex) 블랙

//    mappedBy = "keyboard" → 반대쪽에서 이 필드를 통해 매핑된다는 뜻
//    cascade = CascadeType.ALL → 키보드 저장 시 variant도 같이 저장 가
//    orphanRemoval = true → 키보드에서 variant 제거 시 DB에서도 삭제됨
    @OneToMany(mappedBy = "keyboard", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KeyboardVariant> variants = new ArrayList<>();
}

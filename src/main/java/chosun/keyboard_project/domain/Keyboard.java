package chosun.keyboard_project.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Keyboard {

    //@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String name;
    private String manufacturer;
    private String switchType;
    //private String sound;
    private String keyPressureValue;
    private String keyPressureLabel;
    private String layout;
    //private String material;
    private String backlight;
    private String weightValue;
    private String weightLabel;
    private Integer price;
    private String imageUrl;

    //private String purchaseLink;

    // 관계 매핑
    @ManyToMany
    @JoinTable(
            name = "keyboard_purpose",
            joinColumns = @JoinColumn(name = "keyboard_id"),
            inverseJoinColumns = @JoinColumn(name = "purpose_id")
    )
    private List<Purpose> purposes = new ArrayList<>();

    @ManyToMany //다대다 관계 정의
    @JoinTable( // 두 엔티티 간의 관계를 연결할 중간 테이블을 설정
            name = "keyboard_connection", // 중간 테이블 이름
            joinColumns = @JoinColumn(name = "keyboard_id"),
            inverseJoinColumns = @JoinColumn(name = "connection_id")
    )
    private List<Connection> connections = new ArrayList<>();

    // 기본 생성자, Getter/Setter 등 생략 가능


}

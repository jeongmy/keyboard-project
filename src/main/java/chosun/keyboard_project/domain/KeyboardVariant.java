package chosun.keyboard_project.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class KeyboardVariant {

    @Id // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String purpose;           // 게임용 / 사무용
    private String switchName;        // 적축 / 갈축 등
    private Integer price;
    private String keyPressureValue;  // 43g
    private String keyPressureLabel;  // 43g

    @Column(length = 1000)
    private String imageUrl;

    @Column(length = 1000)
    private String purchaseLink1;
    @Column(length = 1000)
    private String purchaseLink2;
    @Column(length = 1000)
    private String purchaseLink3;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keyboard_id")
    private Keyboard keyboard;
}
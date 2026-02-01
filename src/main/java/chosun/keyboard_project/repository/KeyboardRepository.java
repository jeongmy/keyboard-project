package chosun.keyboard_project.repository;

import chosun.keyboard_project.domain.Keyboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/*
자바에서는 다중 상속이 허용되지 않지만 인터페이스는 가능.
JpaRepository는 Spring Data Jpa에서 제공하는 인터페이스이며 이미 simpleJpaRepository라는 구현체가 존재함
그래서 JpaRepository를 extends만 해도 Spring은 자동으로 simpleJpaRepository를 찾아서 연결해주고 나는
인터페이스만 선언해도 바로 사용할 수 있음.

일반적인 자바 세계에서는 인터페이스는 구현 클래스가 꼭 필요함.
Spring 세계에서는 예를 들어 지금 KeyboardRepository도 결국 인터페이스인데 다른 뭐 예를 들어 KeyboardService에서
바로 private KeyboardRepository keyboardRepository;하고 생성자 @AutoWired해서 사용함.

이게 왜 가능하나?
Spring Data JPA의 동적 프록시(Dynamic Proxy)
Spring은 애플리케이션 시작할 때, KeyboardRepository를 보고:
"JpaRepository를 상속했네? 그럼 내가 구현체 만들어줄게" $JpaRepository는 @Repository 같은 걸 안 붙여도 빈으로 등록됨.
라고 하면서 내부적으로 KeyboardRepository를 구현한 프록시 객체를 만들어서 keyboardRepository 빈으로 등록해줌.
이 구현체는 보통 SimpleJpaRepository라는 클래스에 기반하고, 거기에 네가 만든 @Query나 findBy~ 같은 규칙 기반 메서드도 자동으로 붙여줌.
JpaRepository는 Spring이 제공하는 기본 인터페이스
그 구현체인 SimpleJpaRepository는 Spring이 런타임에 자동으로 연결
난 인터페이스만 선언하면, 자동으로 JPA 기본 메서드들이 동작함

Spring은 내부적으로 KeyboardRepository를 사용할 때,
KeyboardRepositoryCustom을 구현한 클래스 중 이름이 정확히 KeyboardRepositoryImpl인 구현체를 자동으로 찾아서 연결해줌.
여기서 포인트:
Spring은 KeyboardRepository와 이름이 앞부분이 일치하는 Impl 클래스만 자동으로 붙여줌.

이 구조가 왜 필요한가?
KeyboardRepository는 JpaRepository를 상속받기 때문에
스프링 데이터 JPA가 자동으로 기본적인 CRUD는 구현해주고
우리는 복잡한 동적 쿼리(searchKeyboardByFilters) 같은 건
커스텀 인터페이스 + 구현체를 따로 만들어서 책임을 나누는 것.
 */
@Repository
public interface KeyboardRepository extends JpaRepository<Keyboard, Long>, KeyboardRepositoryCustom {
//    @Query("SELECT DISTINCT k FROM Keyboard k " +
//            "LEFT JOIN k.connections c " +
//            "LEFT JOIN k.purposes p " +
//            "WHERE (:weightLabels IS NULL OR k.weightLabel IN :weightLabels) " +
//            "AND (:keyPressureLabels IS NULL OR k.keyPressureLabel IN :keyPressureLabels) " +
//            "AND (:connections IS NULL OR c.label IN :connections) " +
//            "AND (:purposes IS NULL OR p.label IN :purposes) " +
//            "AND (:materials IS NULL OR k.material IN :materials) " +
//            "AND (:layouts IS NULL OR k.layout IN :layouts) " +
//            "AND (:backlights IS NULL OR k.backlight IN :backlights) " +
//            "AND (:switchTypes IS NULL OR k.switchType IN :switchTypes) "+
//            "AND (:manufacturers IS NULL OR k.manufacturer IN :manufacturers) " +
//            "AND (:sounds IS NULL OR k.manufacturer IN :sounds) "
//
//    )
//    List<Keyboard> findByDynamicFilters(
//            @Param("weightLabels") List<String> weightLabels,
//            @Param("keyPressureLabels") List<String> keyPressureLabels,
//            @Param("connections") List<String> connections,
//            @Param("purposes") List<String> purposes,
//            @Param("materials") List<String> materials,
//            @Param("layouts") List<String> layouts,
//            @Param("backlights") List<String> backlights,
//            @Param("switchTypes") List<String> switchTypes,
//            @Param("manufacturers") List<String> manufacturers,
//            @Param("sounds") List<String> sounds
//    );
    @Query("SELECT DISTINCT k FROM Keyboard k LEFT JOIN FETCH k.variants WHERE k.id = :id")
    Optional<Keyboard> findWithVariantsById(@Param("id") Long id);
}

package chosun.keyboard_project.repository;

import chosun.keyboard_project.domain.Keyboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KeyboardRepository extends JpaRepository<Keyboard, Long> {
    @Query("SELECT DISTINCT k FROM Keyboard k " +
            "LEFT JOIN k.connections c " +
            "LEFT JOIN k.purposes p " +
            "WHERE (:weightLabels IS NULL OR k.weightLabel IN :weightLabels) " +
            "AND (:keyPressureLabels IS NULL OR k.keyPressureLabel IN :keyPressureLabels) " +
            "AND (:connections IS NULL OR c.label IN :connections) " +
            "AND (:purposes IS NULL OR p.label IN :purposes) " +
            "AND (:materials IS NULL OR k.material IN :materials) " +
            "AND (:layouts IS NULL OR k.layout IN :layouts) " +
            "AND (:backlights IS NULL OR k.backlight IN :backlights) " +
            "AND (:switchTypes IS NULL OR k.switchType IN :switchTypes) "+
            "AND (:manufacturers IS NULL OR k.manufacturer IN :manufacturers) " +
            "AND (:sounds IS NULL OR k.manufacturer IN :sounds) "

    )
    List<Keyboard> findByDynamicFilters(
            @Param("weightLabels") List<String> weightLabels,
            @Param("keyPressureLabels") List<String> keyPressureLabels,
            @Param("connections") List<String> connections,
            @Param("purposes") List<String> purposes,
            @Param("materials") List<String> materials,
            @Param("layouts") List<String> layouts,
            @Param("backlights") List<String> backlights,
            @Param("switchTypes") List<String> switchTypes,
            @Param("manufacturers") List<String> manufacturers,
            @Param("sounds") List<String> sounds
    );
}

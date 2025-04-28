package chosun.keyboard_project.repository;

import chosun.keyboard_project.domain.Keyboard;
import chosun.keyboard_project.domain.QConnection;
import chosun.keyboard_project.domain.QKeyboard;
import chosun.keyboard_project.domain.QPurpose;
import chosun.keyboard_project.dto.KeyboardFilterRequestDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

public class KeyboardRepositoryImpl implements KeyboardRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    public KeyboardRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<Keyboard> findByQdslFilter(KeyboardFilterRequestDto filterDto) {

        QKeyboard keyboard = QKeyboard.keyboard;
        QConnection connection = QConnection.connection;
        QPurpose purpose = QPurpose.purpose;

        BooleanExpression filter = createFilter(filterDto);

        // QueryDSL을 통해 필터링된 Keyboard 리스트 가져오기
        return queryFactory
                .selectFrom(keyboard)
                .leftJoin(keyboard.connections, connection)
                .leftJoin(keyboard.purposes, purpose)
                .where(filter)
                .distinct()
                .fetch();
    }

    private BooleanExpression createFilter(KeyboardFilterRequestDto filterDto) {

        BooleanExpression filter = QKeyboard.keyboard.isNotNull(); // 기본 필터 (모든 키보드를 포함)

        // 각 조건에 대해 필터를 추가
        if (filterDto.getWeightLabels() != null && !filterDto.getWeightLabels().isEmpty()) {
            filter = filter.and(QKeyboard.keyboard.weightLabel.in(filterDto.getWeightLabels()));
        }
        if (filterDto.getKeyPressureLabels() != null && !filterDto.getKeyPressureLabels().isEmpty()) {
            filter = filter.and(QKeyboard.keyboard.keyPressureLabel.in(filterDto.getKeyPressureLabels()));
        }
        if (filterDto.getConnections() != null && !filterDto.getConnections().isEmpty()) {
            filter = filter.and(QConnection.connection.label.in(filterDto.getConnections()));
        }
        if (filterDto.getPurposes() != null && !filterDto.getPurposes().isEmpty()) {
            filter = filter.and(QPurpose.purpose.label.in(filterDto.getPurposes()));
        }
        if (filterDto.getMaterials() != null && !filterDto.getMaterials().isEmpty()) {
            filter = filter.and(QKeyboard.keyboard.material.in(filterDto.getMaterials()));
        }
        if (filterDto.getLayouts() != null && !filterDto.getLayouts().isEmpty()) {
            filter = filter.and(QKeyboard.keyboard.layout.in(filterDto.getLayouts()));
        }
        if (filterDto.getBacklights() != null && !filterDto.getBacklights().isEmpty()) {
            filter = filter.and(QKeyboard.keyboard.backlight.in(filterDto.getBacklights()));
        }
        if (filterDto.getSwitchTypes() != null && !filterDto.getSwitchTypes().isEmpty()) {
            filter = filter.and(QKeyboard.keyboard.switchType.in(filterDto.getSwitchTypes()));
        }
        if (filterDto.getManufacturers() != null && !filterDto.getManufacturers().isEmpty()) {
            filter = filter.and(QKeyboard.keyboard.manufacturer.in(filterDto.getManufacturers()));
        }
        if (filterDto.getSounds() != null && !filterDto.getSounds().isEmpty()) {
            filter = filter.and(QKeyboard.keyboard.sound.in(filterDto.getSounds()));
        }

        return filter;
    }
}

package chosun.keyboard_project.repository;

import chosun.keyboard_project.domain.Keyboard;
import chosun.keyboard_project.domain.QConnection;
import chosun.keyboard_project.domain.QKeyboard;
import chosun.keyboard_project.domain.QPurpose;
import chosun.keyboard_project.dto.KeyboardFilterRequestDto;
import chosun.keyboard_project.dto.PriceRangeDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class KeyboardRepositoryImpl implements KeyboardRepositoryCustom{
    private final JPAQueryFactory queryFactory;

//- `JPAQueryFactory`는 내부적으로 `EntityManager`를 사용하므로
//- 생성자에서 `EntityManager`를 받아서 초기화
//- Spring이 자동으로 `EntityManager`를 주입해줌
    public KeyboardRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<Keyboard> findByQdslFilter(KeyboardFilterRequestDto filterDto, Pageable pageable) {

//      QueryDSL이 자동 생성한 Q클래스 인스턴스
//      각각 실제 Keyboard, Connection, Purpose 엔티티를 자바 코드로 표현한 객체
//      이걸 통해 .switchType, .label, .manufacturer 같은 필드 접근 가능
//❌ "엔티티 객체"만으로는 QueryDSL 쿼리를 만들 수 없음.
//✅ 반드시 Q타입 객체를 사용해야 함.
//
//🔍 이유
//✅ QueryDSL은 컴파일 타임 타입 안정성을 보장하는 DSL (Domain Specific Language)
//        즉, SQL을 자바 코드처럼 안전하게 작성하게 해주는 도구인데,
//        그걸 가능하게 해주는 게 바로 **Q타입 클래스 (QKeyboard, QConnection, QPurpose)**임.
//
//📌 Q타입이란?
//                예를 들어 Keyboard라는 엔티티가 있으면,
//        QueryDSL이 QKeyboard라는 클래스를 자동 생성해 줌
//
//        이 클래스는 모든 필드(connections, name, layout, ...)를 정적 타입 정보로 표현해줘서
//        IDE 자동완성도 되고, 컴파일 오류도 잡을 수 있게 해줌
//❌ 왜 엔티티 객체는 안 되는가?
//        Keyboard keyboard = new Keyboard();
//          이건 순수한 JPA 엔티티일 뿐이고, QueryDSL이 원하는 “쿼리 조각”이 아님
//          즉, keyboard.name.eq("K6") 같은 코드는 순수 엔티티로는 못 씀
//        구분	        목적	                        QueryDSL에서 사용 가능?
//        Keyboard	  실제 DB와 매핑된 JPA 엔티티	    ❌ 불가능 (쿼리용 아님)
//        QKeyboard	  QueryDSL 전용 쿼리 표현 객체	    ✅ 가능 (쿼리 작성용)
        QKeyboard keyboard = QKeyboard.keyboard;
        QConnection connection = QConnection.connection;
        QPurpose purpose = QPurpose.purpose;

        BooleanBuilder builder = createFilter(filterDto);

        // QueryDSL을 통해 필터링된 Keyboard 리스트 가져오기
        // 🔹 1. 실제 해당 페이지에 해당하는 데이터 조회
        List<Keyboard> content = queryFactory
                .selectFrom(keyboard)
                .leftJoin(keyboard.connections, connection)
                .leftJoin(keyboard.purposes, purpose)
                .where(builder)
                .distinct()
                .offset(pageable.getOffset()) // 몇 번째부터
                .limit(pageable.getPageSize())// 몇 개 가져올지
                .fetch();
        //  .leftJoin(소스 컬렉션, 별칭)
        //  "keyboard가 가지고 있는 connections 컬렉션을
        //   connection이라는 별칭으로 LEFT JOIN 하겠다"
//        QueryDSL은 이 매핑 정보를 보고 자동으로 내부적으로 조인 SQL을 생성합니다.
//        그래서 keyboard.connections 라고 쓰면, 실제로는 keyboard, keyboard_connection, connection을 조인하는 SQL이 됩니다:
//   .leftJoin(keyboard.connections, connection)
//👉 이 말은 "keyboard → keyboard_connection → connection"이라는 경로를 따라 조인하라는 뜻입니다.
//
//        keyboard.connections
//         → @ManyToMany에 의해 keyboard_connection 중간 테이블과 조인
//                (QueryDSL이 자동으로 알아서 JOIN TABLE의 정보를 사용함)
//
//        connection
//         → 중간 테이블을 거쳐 실제 연결 대상인 connection 테이블을 조인
//       즉,
//        keyboard.connections는 중간 테이블 먼저 조인하고,
//                connection은 그 중간 테이블 통해 실제 대상 조인한다 → 맞습니다.
        Long total = queryFactory
                .select(keyboard.count())  // ✅ 핵심
                .from(keyboard)
                .leftJoin(keyboard.connections, connection)
                .leftJoin(keyboard.purposes, purpose)
                .where(builder)
                .fetchOne();  // Long 값 하나만 나옴
        // fetchOne()은 조회 결과가 없으면 null을 반환함 때문에 return에서 null 체크 후 아니면 그대로, 맞으면 0 리턴
        return new PageImpl<>(content, pageable, total != null ? total : 0);

    }

    private BooleanBuilder createFilter(KeyboardFilterRequestDto filterDto) {
//        이건 기본 조건으로, 항상 참이 되도록 만드는 "기초 필터"
//        왜냐하면 조건이 아무것도 없는 경우에도 .where() 안에 뭔가는 넣어야 하니까.
//        AND로 계속 이어붙이기 위해 시작점으로 사용한 것.
        //BooleanExpression filter = QKeyboard.keyboard.isNotNull(); // 기본 필터 (모든 키보드를 포함)
        BooleanBuilder builder = new BooleanBuilder();

        if (filterDto.getWeightLabels() != null && !filterDto.getWeightLabels().isEmpty()) {
            builder.and(QKeyboard.keyboard.weightLabel.in(filterDto.getWeightLabels()));
        }
        if (filterDto.getKeyPressureLabels() != null && !filterDto.getKeyPressureLabels().isEmpty()) {
            builder.and(QKeyboard.keyboard.keyPressureLabel.in(filterDto.getKeyPressureLabels()));
        }
        if (filterDto.getConnections() != null && !filterDto.getConnections().isEmpty()) {
            builder.and(QConnection.connection.label.in(filterDto.getConnections()));
        }
        if (filterDto.getPurposes() != null && !filterDto.getPurposes().isEmpty()) {
            builder.and(QPurpose.purpose.label.in(filterDto.getPurposes()));
        }
        if (filterDto.getMaterials() != null && !filterDto.getMaterials().isEmpty()) {
            builder.and(QKeyboard.keyboard.material.in(filterDto.getMaterials()));
        }
        if (filterDto.getLayouts() != null && !filterDto.getLayouts().isEmpty()) {
            builder.and(QKeyboard.keyboard.layout.in(filterDto.getLayouts()));
        }
        if (filterDto.getBacklights() != null && !filterDto.getBacklights().isEmpty()) {
            builder.and(QKeyboard.keyboard.backlight.in(filterDto.getBacklights()));
        }
        if (filterDto.getSwitchTypes() != null && !filterDto.getSwitchTypes().isEmpty()) {
            builder.and(QKeyboard.keyboard.switchType.in(filterDto.getSwitchTypes()));
        }
        if (filterDto.getManufacturers() != null && !filterDto.getManufacturers().isEmpty()) {
            builder.and(QKeyboard.keyboard.manufacturer.in(filterDto.getManufacturers()));
        }
        if (filterDto.getSounds() != null && !filterDto.getSounds().isEmpty()) {
            builder.and(QKeyboard.keyboard.sound.in(filterDto.getSounds()));
        }
        if (filterDto.getPriceRanges() != null && !filterDto.getPriceRanges().isEmpty()) {
            BooleanBuilder priceBuilder = new BooleanBuilder();
            for (PriceRangeDTO range : filterDto.getPriceRanges()) {
                BooleanBuilder singleRange = new BooleanBuilder();
                if (range.getMin() != null) {
                    singleRange.and(QKeyboard.keyboard.price.goe(range.getMin()));
                }
                if (range.getMax() != null) {
                    singleRange.and(QKeyboard.keyboard.price.loe(range.getMax()));
                }
                priceBuilder.or(singleRange);
            }
            builder.and(priceBuilder);
        }

        return builder;
    }
}

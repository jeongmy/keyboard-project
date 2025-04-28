package chosun.keyboard_project.service;

import chosun.keyboard_project.JwtTokenProvider;
import chosun.keyboard_project.domain.Connection;
import chosun.keyboard_project.domain.Keyboard;
import chosun.keyboard_project.domain.Purpose;
import chosun.keyboard_project.dto.KeyboardDto;
import chosun.keyboard_project.dto.KeyboardFilterRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import chosun.keyboard_project.repository.KeyboardRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class KeyboardService {
    private final KeyboardRepository keyboardRepository;

    @Autowired
    public KeyboardService(KeyboardRepository keyboardRepository) {
        this.keyboardRepository = keyboardRepository;
    }

    public List<KeyboardDto> getFilteredKeyboards(List<String> weightLabels,
                                                  List<String> keyPressureLabels,
                                                  List<String> connections,
                                                  List<String> purposes,
                                                  List<String> materials,
                                                  List<String> layouts,
                                                  List<String> backlights,
                                                  List<String> switchTypes,
                                                  List<String> manufacturers,
                                                  List<String> sounds
                                                  ) {

        // 필터 조건을 키보드 리포지토리의 메서드로 전달
        List<Keyboard> keyboards = keyboardRepository.findByDynamicFilters(
                weightLabels, keyPressureLabels, connections, purposes,
                materials, layouts, backlights, switchTypes, manufacturers,sounds
        );

        // 엔티티를 DTO로 변환 후 반환
        return keyboards.stream()
                .map(this::convertToDto)
                .toList();
    }

    // 엔티티를 DTO로 변환하는 메서드
    private KeyboardDto convertToDto(Keyboard keyboard) {

        List<String> connectionLabels = keyboard.getConnections().stream().
                map(Connection::getLabel).toList();

        List<String> purposeLabels = keyboard.getPurposes().stream().
                map(Purpose::getLabel).toList();

        return new KeyboardDto(
                keyboard.getId(),
                keyboard.getName(),
                keyboard.getManufacturer(),
                keyboard.getSwitchType(),
                keyboard.getSound(),
                keyboard.getKeyPressureValue(),
                keyboard.getKeyPressureLabel(),
                keyboard.getLayout(),
                keyboard.getMaterial(),
                keyboard.getBacklight(),
                keyboard.getWeightValue(),
                keyboard.getWeightLabel(),
                keyboard.getPrice(),
                connectionLabels,
                purposeLabels

        );
    }

    public List<KeyboardDto> filterKeyboardsByQdsl(KeyboardFilterRequestDto filterDto){
        // QueryDSL을 사용한 필터링
        List<Keyboard> keyboards = keyboardRepository.findByQdslFilter(filterDto);

        // Keyboard 엔티티를 KeyboardDto로 변환하여 반환
        return keyboards.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

}

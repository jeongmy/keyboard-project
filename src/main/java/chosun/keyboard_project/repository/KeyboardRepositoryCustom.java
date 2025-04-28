package chosun.keyboard_project.repository;

import chosun.keyboard_project.domain.Keyboard;
import chosun.keyboard_project.dto.KeyboardFilterRequestDto;

import java.util.List;

public interface KeyboardRepositoryCustom {
    List<Keyboard> findByQdslFilter(KeyboardFilterRequestDto filterDto);
}

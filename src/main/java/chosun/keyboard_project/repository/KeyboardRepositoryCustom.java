package chosun.keyboard_project.repository;

import chosun.keyboard_project.domain.Keyboard;
import chosun.keyboard_project.dto.keyboardDTO.KeyboardFilterRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface KeyboardRepositoryCustom {
    Page<Keyboard> findByQdslFilter(KeyboardFilterRequestDTO filterDto, String sort, Pageable pageable);
}

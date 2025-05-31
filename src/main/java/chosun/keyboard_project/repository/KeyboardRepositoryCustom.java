package chosun.keyboard_project.repository;

import chosun.keyboard_project.domain.Keyboard;
import chosun.keyboard_project.dto.KeyboardFilterRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface KeyboardRepositoryCustom {
    Page<Keyboard> findByQdslFilter(KeyboardFilterRequestDto filterDto, String sort, Pageable pageable);
}

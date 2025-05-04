package chosun.keyboard_project.controller;

import chosun.keyboard_project.domain.Keyboard;
import chosun.keyboard_project.dto.KeyboardDto;
import chosun.keyboard_project.dto.KeyboardFilterRequestDto;
import chosun.keyboard_project.repository.KeyboardRepository;
import oracle.ucp.proxy.annotation.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import chosun.keyboard_project.service.KeyboardService;

import java.util.List;

@RestController
@RequestMapping("/keyboards")
public class KeyboardController {

    private final KeyboardService keyboardService;

    @Autowired
    public KeyboardController(KeyboardService keyboardService) {
        this.keyboardService = keyboardService;
    }

    @GetMapping("/hello")
    public ResponseEntity<String> hell(){
        String h = "helloB";
        return ResponseEntity.ok(h);
    }

    @GetMapping("/{id}")
    public ResponseEntity<KeyboardDto> getKeyboard(@PathVariable("id") long id){
        return ResponseEntity.ok(keyboardService.getKeyboard(id));
    }

    @PostMapping("/filter/jpa")
    public List<KeyboardDto> filterKeyboards(@RequestBody KeyboardFilterRequestDto filterDto) {
        return keyboardService.getFilteredKeyboards(
                filterDto.getWeightLabels(),
                filterDto.getKeyPressureLabels(),
                filterDto.getConnections(),
                filterDto.getPurposes(),
                filterDto.getMaterials(),
                filterDto.getLayouts(),
                filterDto.getBacklights(),
                filterDto.getSwitchTypes(),
                filterDto.getManufacturers(),
                filterDto.getSounds()
        );
    }
//  예외 처리
//  ❌ 필터 결과가 아무 것도 없을 경우	서비스	200 OK + 빈 리스트 or 404
//  ❌ DTO가 아예 null로 들어옴	컨트롤러	400 Bad Request
    @PostMapping("/filter/qdsl")
    public ResponseEntity<List<KeyboardDto>> filterKeyboardsByQdsl(@RequestBody KeyboardFilterRequestDto filterDto) {
        return ResponseEntity.ok(keyboardService.filterKeyboardsByQdsl(filterDto));
    }


}

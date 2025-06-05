package chosun.keyboard_project.controller;

import chosun.keyboard_project.dto.keyboardDTO.KeyboardDto;
import chosun.keyboard_project.dto.keyboardDTO.KeyboardFilterRequestDTO;
import chosun.keyboard_project.dto.PagedResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import chosun.keyboard_project.service.KeyboardService;

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

//  예외 처리
//  ❌ 필터 결과가 아무 것도 없을 경우	서비스	200 OK + 빈 리스트 or 404
//  ❌ DTO가 아예 null로 들어옴	컨트롤러	400 Bad Request
    @PostMapping("/filter/qdsl")
    public ResponseEntity<PagedResponseDTO<KeyboardDto>> filterKeyboardsByQdsl(
            @RequestBody KeyboardFilterRequestDTO filterDto,
            @RequestParam(name= "sort",defaultValue = "DEFAULT") String sort,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size
    ) {
        return ResponseEntity.ok(new PagedResponseDTO<>(keyboardService.filterKeyboardsByQdsl(filterDto, sort, page, size)));
    }

//    @PostMapping("/search")
//    public ResponseEntity<PagedResponseDTO<KeyboardDto>> search(
//            @RequestParam(name = "statement") String statement,
//            @RequestParam(name = "sort", defaultValue = "DEFAULT") String sort,
//            @RequestParam(name = "page", defaultValue = "0") int page,
//            @RequestParam(name = "size", defaultValue = "5") int size
//    ){
//        Pageable pageable = PageRequest.of(page, size);
//        keyboardService.search(statement, pageable);
//    }



}

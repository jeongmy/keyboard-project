package chosun.keyboard_project.controller;

import chosun.keyboard_project.dto.CommentResponseDTO;
import chosun.keyboard_project.dto.keyboardDTO.KeyboardDto;
import chosun.keyboard_project.dto.keyboardDTO.KeyboardFilterRequestDTO;
import chosun.keyboard_project.dto.PagedResponseDTO;
import chosun.keyboard_project.repository.CommentRepository;
import chosun.keyboard_project.service.CommentService;
import chosun.keyboard_project.service.CustomUserDetails;
import chosun.keyboard_project.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import chosun.keyboard_project.service.KeyboardService;

import java.util.List;

@RestController
@RequestMapping("/keyboards")
public class KeyboardController {

    private final KeyboardService keyboardService;
    private final CommentService commentService;
    private final FavoriteService favoriteService;

    @Autowired
    public KeyboardController(KeyboardService keyboardService, CommentService commentService, FavoriteService favoriteService) {
        this.keyboardService = keyboardService;
        this.commentService = commentService;
        this.favoriteService = favoriteService;
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
//  필터 결과가 아무 것도 없을 경우	서비스	200 OK + 빈 리스트 or 404
//  DTO가 아예 null로 들어옴	컨트롤러	400 Bad Request
    @PostMapping("/filter/qdsl")
    public ResponseEntity<PagedResponseDTO<KeyboardDto>> filterKeyboardsByQdsl(
            @RequestBody KeyboardFilterRequestDTO filterDto,
            @RequestParam(name= "sort",defaultValue = "DEFAULT") String sort,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size
    ) {
        return ResponseEntity.ok(new PagedResponseDTO<>(keyboardService.filterKeyboardsByQdsl(filterDto, sort, page, size)));
    }

    @GetMapping("/search")
    public ResponseEntity<PagedResponseDTO<KeyboardDto>> searchKeyboards(
            @RequestParam(name = "statement") String statement,
            @RequestParam(name= "sort",defaultValue = "DEFAULT") String sort,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size
    ) {

        if (statement == null || statement.trim().isEmpty()) {
            throw new IllegalArgumentException("검색어는 필수 입력값입니다.");
        }

        return ResponseEntity.ok(new PagedResponseDTO<>(keyboardService.searchKeyboards(statement, sort, page, size)));
    }

    @GetMapping("/{keyboardId}/comments")
    public ResponseEntity<List<CommentResponseDTO>> getCommentsByKeyboard(@PathVariable(name = "keyboardId") Long keyboardId) {
        return ResponseEntity.ok(commentService.getCommentsByKeyboard(keyboardId));
    }

    @PostMapping("/{id}/favorite")
    public ResponseEntity<Boolean> addFavorite(@PathVariable("id") Long keyboardId,
                                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        favoriteService.addFavorite(keyboardId, userDetails.getUsername());

        return ResponseEntity.ok(true); // 찜됨
    }

    @DeleteMapping("/{id}/favorite")@Transactional
    public ResponseEntity<Boolean> removeFavorite(@PathVariable("id") Long keyboardId,
                                                  @AuthenticationPrincipal CustomUserDetails userDetails) {
        favoriteService.removeFavorite(keyboardId, userDetails.getUsername());
        return ResponseEntity.ok(false); // 찜 해제됨
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<KeyboardDto>> getMyFavorites(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(favoriteService.getFavorites(userDetails.getUsername()));
    }


}

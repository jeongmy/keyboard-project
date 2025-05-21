package chosun.keyboard_project.controller;

import chosun.keyboard_project.dto.KeyboardFilterRequestDto;
import chosun.keyboard_project.service.GptService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/gpt")
public class GptController {

    private final GptService gptService;

    public GptController(GptService gptService) {
        this.gptService = gptService;
    }

    @PostMapping("/filter")
    public ResponseEntity<?> filter(@RequestBody Map<String, String> body) {
        System.out.println("GPT 호출!!!");
        String message = body.get("message");
//        KeyboardFilterRequestDto filterDto = gptService.handleUserInput(message);
//        return ResponseEntity.ok(filterDto);

        try {
            KeyboardFilterRequestDto filterDto = gptService.handleUserInput(message);
            return ResponseEntity.ok(filterDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("GPT 응답 분석에 실패했습니다.");
        }
    }

    @PostMapping("/analyze")
    public ResponseEntity<String> analyze(@RequestBody Map<String, String> body) {
        System.out.println("GPT 호출 완료");
        String userInput = body.get("message");
        String response = gptService.extractFilters(userInput);
        System.out.println("GPT 응답 완료");
        return ResponseEntity.ok(response);
    }

}

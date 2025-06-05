package chosun.keyboard_project.controller;

import chosun.keyboard_project.dto.gptDTO.MbtiAnswerDTO;
import chosun.keyboard_project.dto.gptDTO.MbtiGptResponseDTO;
import chosun.keyboard_project.service.MbtiGptService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/mbti")
public class MbtiController {
    private final MbtiGptService mbtiGptService;

    public MbtiController(MbtiGptService mbtiGptService){
        this.mbtiGptService = mbtiGptService;
    }

    @PostMapping("/interpret")
    public ResponseEntity<MbtiGptResponseDTO> interpretMbti(@RequestBody List<MbtiAnswerDTO> answers) {
        System.out.println("📥 MBTI 질문 + 응답 리스트 수신됨");
        printMbtiAnswers(answers);  // ⬅️ 여기에 출력 메서드 호출
        MbtiGptResponseDTO result = mbtiGptService.sendToGptAndInterpret(answers);
        return ResponseEntity.ok(result);
    }

    private void printMbtiAnswers(List<MbtiAnswerDTO> answers) {
        System.out.println("🧠 사용자 MBTI 응답 내용:");
        for (int i = 0; i < answers.size(); i++) {
            MbtiAnswerDTO a = answers.get(i);
            System.out.printf("%2d. 질문: %s%n", i + 1, a.getQuestion());
            System.out.printf("    응답: %s%n%n", a.getAnswer());
        }
    }

}

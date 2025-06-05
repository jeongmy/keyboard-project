package chosun.keyboard_project.service;

import chosun.keyboard_project.dto.gptDTO.GptFilterDTO;
import chosun.keyboard_project.dto.keyboardDTO.KeyboardFilterRequestDTO;
import chosun.keyboard_project.dto.gptDTO.MbtiAnswerDTO;
import chosun.keyboard_project.dto.gptDTO.MbtiGptResponseDTO;
import chosun.keyboard_project.gpt_utill.GptClient;
import chosun.keyboard_project.gpt_utill.GptMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MbtiGptService {

    private final GptClient gptClient; // GPT API 호출 담당 클래스

    public MbtiGptService(GptClient gptClient){
        this.gptClient = gptClient;
    }

    public MbtiGptResponseDTO sendToGptAndInterpret(List<MbtiAnswerDTO> answers) {
        String prompt = generatePromptFromAnswers(answers);
        String gptResponse = gptClient.callGpt(prompt);
        return parseGptResponse(gptResponse);
    }

    private String generatePromptFromAnswers(List<MbtiAnswerDTO> answers) {
        StringBuilder sb = new StringBuilder();
        // [1] 시스템 역할 정의
        sb.append("당신은 키보드 추천을 위해 사용자의 MBTI 성향을 바탕으로 필터 값을 추출하는 어시스턴트입니다.\n");
        sb.append("당신의 목표는 아래 나열된 필드와 허용된 값만 사용하여 JSON 객체를 생성하는 것입니다.\n");
        sb.append("모든 필드를 사용할 필요는 없지만, 필요하다고 판단되는 항목은 포함해 주세요.\n");
        sb.append("한 필드에 여러 값을 넣어도 되고 안 넣어도 돼.\n\n");
        sb.append("필터를 만든 후에는 사용자가 이런 키보드를 선호할 것같은 이유를 설명해 주세요.\n\n");

        sb.append("❗ 반드시 아래 허용된 값만 사용해야 합니다:\n");
        sb.append("- priceRanges: [\"null~49999\"], [\"50000~99999\"], [\"100000~149999\"], [\"150000~null\"]\n");
        sb.append("- weightLabels: [\"가벼운\", \"보통\", \"무거운\"]\n");
        sb.append("- connections: [\"유선\", \"무선\", \"유선+무선\"]\n");
        sb.append("- purposes: [\"게임용\", \"사무용\"]\n");
        sb.append("- layouts: [\"풀배열\", \"텐키리스\"]\n");
        sb.append("- backlights: [\"레인보우 백라이트\", \"RGB 백라이트\", \"없음\"]\n");
        sb.append("- switchTypes: [\"리니어\", \"택타일\", \"클릭\"]\n");
        sb.append("- manufacturers: [\"ASUS\", \"AULA\", \"COX\", \"Ducky\", \"FL\", \"Keydous\", \"LEOBOG\", \"MCHOSE\", \"MOUNTAIN\", \"NZXT\", \"QSENN\", \"Razer\", \"Riccks\", \"VARMILO\", \"darkFlash\", \"다얼유\", \"마이크로닉스\", \"발키리\", \"웨이코스\", \"주연테크\", \"쿨러마스터\"]\n\n");

        // [2] 사용자 질문+답변
        sb.append("다음은 사용자의 MBTI 성향을 파악하기 위한 12개의 질문과 응답입니다:\n\n");

        for (int i = 0; i < answers.size(); i++) {
            MbtiAnswerDTO a = answers.get(i);
            sb.append((i + 1)).append(". 질문: ").append(a.getQuestion()).append("\n");
            sb.append("   응답: ").append(a.getAnswer()).append("\n\n");
        }

        // [3] 출력 포맷 안내
        sb.append("출력은 아래 형식처럼 JSON으로 해 주세요:\n");
        sb.append("{\n  \"filter\": {\n    ... 키보드 필터 값들 ...\n  },\n  \"analysis\": \"... 이 사용자가 이런 키보드를 선호하는 이유 설명 ...\"\n}");

        return sb.toString();
    }

    private MbtiGptResponseDTO parseGptResponse(String json) {
        try {
            System.out.println("✅ GPT Content 원본:");
            System.out.println(json);

            // GPT 응답에서 ```json ~ ``` 제거
            json = json.trim();
            if (json.startsWith("```")) {
                json = json.replaceAll("```json", "")
                        .replaceAll("```", "")
                        .trim();
            }

            // 1. 전체 JSON 파싱 (GPT가 준 응답: 필터 + 분석 설명)
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);

            // 2. filter 부분 파싱 → GptFilterDto
            JsonNode filterNode = root.path("filter");
            GptFilterDTO gptFilterDto = mapper.treeToValue(filterNode, GptFilterDTO.class);

            // 3. analysis 텍스트 추출
            String analysis = root.path("analysis").asText();

            // 4. GptFilterDto → KeyboardFilterRequestDto 변환
            KeyboardFilterRequestDTO keyboardFilterDto = GptMapper.toKeyboardFilterDto(gptFilterDto);

            // 5. MbtiGptResponseDto 구성
            MbtiGptResponseDTO result = new MbtiGptResponseDTO();
            result.setFilter(keyboardFilterDto);
            result.setAnalysis(analysis);

            return result;

        } catch (Exception e) {
            System.err.println("❌ GPT 응답 파싱 실패");
            e.printStackTrace();
            throw new RuntimeException("GPT 응답 파싱 실패");
        }
    }
}

package chosun.keyboard_project.service;

import chosun.keyboard_project.gpt_utill.GptMapper;
import chosun.keyboard_project.dto.gptDTO.GptFilterDTO;
import chosun.keyboard_project.dto.keyboardDTO.KeyboardFilterRequestDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class GptService {
    private final WebClient webClient;

    public GptService(WebClient openAiWebClient) {
        this.webClient = openAiWebClient;
    }

    public KeyboardFilterRequestDTO handleUserInput(String userInput) {
        GptFilterDTO gptFilterDto = extractFilterDto(userInput); // GPT 호출 후 응답 파싱
        if (gptFilterDto == null) {
            throw new IllegalStateException("GPT 응답 파싱 실패 또는 필터 정보 없음");
        }
        return GptMapper.toKeyboardFilterDto(gptFilterDto);
    }

    public GptFilterDTO extractFilterDto(String userInput) {
        String prompt = """
당신은 키보드 추천을 위해 사용자의 자연어 요청에서 필터 값을 추출하는 어시스턴트입니다.

당신의 목표는 아래 나열된 필드와 허용된 값을 사용하여 JSON 객체를 생성하는 것입니다.

사용자의 문장에서 의도를 파악해서 너가 지식을 동반해서 필드에 잘 값을 추가해.
단, 아래의 허용된 값만 사용할 수 있어, 오직 아래의 허용된 값에서만 선택해야해.

- priceRanges: ["null~99999"], ["100000~null"]
- weightLabels: ["가벼운", "보통", "무거운"]
- connections: ["유선", "무선", "유선+무선"]
- purposes: ["게임용", "사무용"]
- layouts: ["풀배열", "텐키리스"]
- backlights: ["레인보우 백라이트", "RGB 백라이트", "없음"]
- switchTypes: ["리니어", "택타일", "클릭"]
- manufacturers: ["한성키보드", "로지텍", "CHERRY", "앱코", "CORSAIR", "ASUS", "AULA", "COX", "Ducky", "FL", "LEOBOG", "MCHOSE", "MOUNTAIN", "NZXT", "QSENN", "Razer", "Riccks", "VARMILO", "darkFlash", "다얼유", "마이크로닉스", "발키리", "웨이코스", "주연테크", "쿨러마스터"]

너에게 도움이 될만한 지식을 줄게.
1. priceRanges는 가격과 관련된 필드이고 관련된 키워드가 없으면 절대 값을 추가하지마.
2. weightLabels는 언급이 없으면 선택하지 말고 관련된 단어가 나올 때에만 추가해.
3. connections는 사용자가 언급을 하면 그에 맞는 하나만 선택하고 언급이 없으면 절대 선택하지마.
4. purposes에서 사무용은 조용한 키보드고 게임용은 조금 시끄러운 특징이 있어. 관련된 단어가 있을 때에만 값을 추가해. 가격과는 상관없어.
5. layouts는 언급이 없으면 값을 추가하지마
6. backlights는 될 수 있으면 값을 추가하지마. 사용자의 자연어에서 필요할 거 같다고 생각하면 넣어.
7. switchTypes는 너가 사용자의 요구를 보고 추측할 수 있을 때에만 값을 추가해.
8. manufacturers도 가능한 많이 값을 추가해.

추가로 참고할만한 지식을 줄게.
1. "조약돌" 또는 "보글보글"이라는 단어나, 관련된 단어가 나오면 switchTypes에 "리니어"를 추가해.
2. "찰칵"이라는 단어나, 관련된 단어가 나오면 switchTypes에 "청축"을 추가해.

각 필드는 반드시 배열 [] 형식으로 응답해.
모든 필드를 사용할 필요는 없지만 필요하다고 생각되면 넣어.
한 필드에 여러 값을 넣어도 되고 안 넣어도 돼.

Return only a valid JSON object that reflects the user's intent.

🗣 User input: "%s"
""".formatted(userInput);


        Map<String, Object> request = Map.of(
                "model", "gpt-4o-mini",
                "messages", List.of(
                        Map.of("role", "user", "content", prompt)
                )
        );

        String response = webClient.post()
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        try {
            // 1. 전체 응답 파싱
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);
            String contentJson = root
                    .path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText();

            // 혹시 ```json 같은 포맷 있으면 제거
            contentJson = contentJson.trim();
            if (contentJson.startsWith("```")) {
                contentJson = contentJson.replaceAll("```json", "")
                        .replaceAll("```", "")
                        .trim();
            }

            // 2. content 안의 JSON 파싱
            return mapper.readValue(contentJson, GptFilterDTO.class);

        } catch (Exception e) {
            System.err.println("❌ GPT 응답 파싱 실패 - 사용자 입력: " + userInput);
            System.err.println("❌ GPT 응답 원문: " + response);
            e.printStackTrace();
            return null;
        }
    }

}

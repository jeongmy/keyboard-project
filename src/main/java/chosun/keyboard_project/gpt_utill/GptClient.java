package chosun.keyboard_project.gpt_utill;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Component
public class GptClient {

    private final WebClient openAiWebClient;

    public GptClient(WebClient openAiWebClient){
        this.openAiWebClient = openAiWebClient;
    }

    public String callGpt(String prompt) {
//        String requestBody = buildRequestBody(prompt);
//
//        String response = openAiWebClient.post()
//                .bodyValue(requestBody)
//                .retrieve()
//                .bodyToMono(String.class)
//                .block();
//
//        return extractContentFromResponse(response);
//
        Map<String, Object> requestBody = Map.of(
                "model", "gpt-4o",
                "messages", List.of(
                        Map.of("role", "user", "content", prompt)
                )
        );

        String response = openAiWebClient.post()
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return extractContentFromResponse(response);
    }

    private String buildRequestBody(String prompt) {
        return """
        {
          "model": "gpt-4o-mini",
          "messages": [
            {
              "role": "system",
              "content": "너는 키보드 추천 도우미야. 응답은 무조건 JSON 형태로 해줘."
            },
            {
              "role": "user",
              "content": "%s"
            }
          ]
        }
        """.formatted(prompt);
    }

    private String extractContentFromResponse(String responseJson) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(responseJson);
            String content = root.path("choices").get(0).path("message").path("content").asText();
            return content;
        } catch (Exception e) {
            throw new RuntimeException("GPT 응답 파싱 실패", e);
        }
    }
}

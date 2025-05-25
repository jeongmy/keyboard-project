package chosun.keyboard_project.gpt_utill;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class OpenAiConfig {
    @Value("${openai.api-key}")
    private String apiKey;

    @Bean
    public WebClient openAiWebClient() {

        System.out.println("✅ OpenAI API Key: " + apiKey);

        return WebClient.builder()
                .baseUrl("https://api.openai.com/v1/chat/completions")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}

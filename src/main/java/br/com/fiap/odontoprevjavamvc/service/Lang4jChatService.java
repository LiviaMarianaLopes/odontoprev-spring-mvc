package br.com.fiap.odontoprevjavamvc.service;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import org.springframework.stereotype.Service;

@Service
public class Lang4jChatService {
    private static ChatLanguageModel connectModel() {
        return OllamaChatModel.builder()
                .baseUrl("http://localhost:11434")
                .modelName("gemma3:1b")
                .build();
    }

    public String run(String userPrompt) {
        var model = connectModel();
        return  model.generate(userPrompt);
    }
}
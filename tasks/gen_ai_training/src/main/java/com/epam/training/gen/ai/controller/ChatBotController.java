package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.model.ChatBotResponse;
import com.epam.training.gen.ai.model.Prompt;
import com.epam.training.gen.ai.service.SemanticKernelService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ChatBotController {

    private SemanticKernelService semanticKernelService;

    @PostMapping("chat-bot/prompt")
    public ChatBotResponse promptChatBot(@RequestBody Prompt request) {
        var response = semanticKernelService.processPrompt(request.input());
        return new ChatBotResponse(response);
    }
}

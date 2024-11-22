package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.model.ChatBotResponse;
import com.epam.training.gen.ai.model.Prompt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatBotController {

    @PostMapping("chat-bot/prompt")
    public ChatBotResponse promptChatBot(@RequestBody Prompt request) {

        return new ChatBotResponse("response");
    }
}

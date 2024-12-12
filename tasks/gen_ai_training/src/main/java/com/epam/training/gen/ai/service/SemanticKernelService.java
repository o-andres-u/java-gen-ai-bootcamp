package com.epam.training.gen.ai.service;

import com.epam.training.gen.ai.model.Prompt;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import com.microsoft.semantickernel.orchestration.ToolCallBehavior;
import com.microsoft.semantickernel.semanticfunctions.KernelFunction;
import com.microsoft.semantickernel.semanticfunctions.KernelFunctionArguments;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class SemanticKernelService {

    private final Kernel kernel;
    private final ChatHistory chatHistory;
    private final DialService dialService;

    @Value("${client.openai.deployment.name}")
    private String defaultDeploymentName;

    public SemanticKernelService(Kernel kernel, ChatHistory chatHistory, DialService dialService) {
        this.kernel = kernel;
        this.chatHistory = chatHistory;
        this.dialService = dialService;
    }

    public String processPrompt(@NonNull Prompt prompt) {
        verifyModelSupport(prompt.model());

        var promptExecutionSettings = buildPromptExecutionSettings(prompt.model(), prompt.temperature(), prompt.maxTokens());
        var response = kernel.invokeAsync(getKernelFunction())
                .withPromptExecutionSettings(promptExecutionSettings)
                .withArguments(createFunctionArguments(prompt.input()))
                .withToolCallBehavior(
                        ToolCallBehavior.allowOnlyKernelFunctions(false)
                   )
                .withResultType(String.class)
                .block();

        chatHistory.addUserMessage(prompt.input());
        assert response != null;
        chatHistory.addAssistantMessage(response.getResult());

        log.info("AI answer: {}", response.getResult());
        return response.getResult();
    }

    private void verifyModelSupport(String modelName) {
        if (modelName != null && !dialService.getSupportedModels().contains(modelName)) {
            throw new IllegalArgumentException("The provided model is not supported");
        }
    }

    private PromptExecutionSettings buildPromptExecutionSettings(String modelName, double temperature, int maxTokens) {
        var modelId = modelName != null ? modelName : defaultDeploymentName;
        return PromptExecutionSettings.builder()
                .withModelId(modelId)
                .withTemperature(temperature)
                .withMaxTokens(maxTokens)
                .build();
    }

    private KernelFunction<Object> getKernelFunction() {
        return KernelFunction.createFromPrompt(
            """
                <message role="system">You are a time traveler robot who comes from 2063. Your main goal is to find a person named Arus.</message>
    
                {{ConversationSummaryPlugin-summarize_conversation history}}
    
                <message role="user">{{request}}</message>
             """)
            .withTemplateFormat("handlebars")
            .build();
    }

    private KernelFunctionArguments createFunctionArguments(String userPrompt) {
        return KernelFunctionArguments.builder()
                .withVariable("history", chatHistory)
                .withVariable("request", userPrompt)
                .build();
    }
}

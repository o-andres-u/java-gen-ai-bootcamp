package com.epam.training.gen.ai.service;

import com.epam.training.gen.ai.model.Prompt;
import com.epam.training.gen.ai.plugins.PromptFunctionConstants;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import com.microsoft.semantickernel.orchestration.ToolCallBehavior;
import com.microsoft.semantickernel.semanticfunctions.KernelFunction;
import com.microsoft.semantickernel.semanticfunctions.KernelFunctionArguments;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class SemanticKernelService {

    private final Kernel kernel;
    private final PromptExecutionSettings promptExecutionSettings;
    private final ChatHistory chatHistory;

    private DialService dialService;

    public String processPrompt(@NonNull Prompt prompt) {
        verifyModelSupport(prompt.model());

        var summarizeConversationFunction = Objects.requireNonNull(kernel.getPlugin(PromptFunctionConstants.CONVERSATION_SUMMARY_PLUGIN))
                .get(PromptFunctionConstants.SUMMARIZE_CONVERSATION);

        var response = kernel.invokeAsync(getKernelFunction())
                .withPromptExecutionSettings(promptExecutionSettings)
                .withArguments(createFunctionArguments(prompt.input()))
                .withToolCallBehavior(
                        ToolCallBehavior.allowOnlyKernelFunctions(true, summarizeConversationFunction)
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
        if (StringUtils.isBlank(modelName) || !dialService.getSupportedModels().contains(modelName)) {
            throw new IllegalArgumentException("The provided model is not supported");
        }
    }

    private KernelFunction<Object> getKernelFunction() {
        return KernelFunction.createFromPrompt(
            """
                <message role="system">You are a time traveler robot who comes from 2063. Your main goal is to find a person named Arus.</message>
                <message role="system">Instructions: Respond to the user prompt if your sure that it is question.
                    Try to respond briefly, in only one paragraph with no more than 100 words.
                    If you find that's not a question, replay back saying that you need to leave mentioning who you are
                    and who you are looking for.</message>
    
                {{ConversationSummaryPlugin-SummarizeConversation history}}
    
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

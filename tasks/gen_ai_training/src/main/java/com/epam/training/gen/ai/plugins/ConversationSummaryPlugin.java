package com.epam.training.gen.ai.plugins;

import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.contextvariables.ContextVariableTypes;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import com.microsoft.semantickernel.semanticfunctions.KernelFunction;
import com.microsoft.semantickernel.semanticfunctions.KernelFunctionArguments;
import com.microsoft.semantickernel.semanticfunctions.annotations.DefineKernelFunction;
import com.microsoft.semantickernel.semanticfunctions.annotations.KernelFunctionParameter;
import com.microsoft.semantickernel.text.TextChunker;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class ConversationSummaryPlugin {

    private final static int MAX_TOKENS = 1024;
    private final static double TEMPERATURE = 0.1d;
    private final static double TOP_P = 0.5d;

    private final KernelFunction<String> summarizeConversationFunction;

    public ConversationSummaryPlugin() {
        var conversationSummaryExecutionSettings = PromptExecutionSettings.builder()
                .withMaxTokens(MAX_TOKENS)
                .withTemperature(TEMPERATURE)
                .withTopP(TOP_P)
                .build();

        this.summarizeConversationFunction = KernelFunction
                .<String>createFromPrompt(PromptFunctionConstants.SummarizeConversationDefinition)
                .withDefaultExecutionSettings(conversationSummaryExecutionSettings)
                .withName(PromptFunctionConstants.SUMMARIZE_CONVERSATION)
                .withDescription("Given a section of a conversation transcript, summarize the part of the conversation.")
                .build();
    }

    @DefineKernelFunction(description = "Given a long conversation transcript, summarize the conversation.", name = PromptFunctionConstants.SUMMARIZE_CONVERSATION, returnType = "java.lang.String")
    public Mono<String> summarizeConversationAsync(
            @KernelFunctionParameter(description = "A long conversation transcript.", name = "input") String input,
            Kernel kernel) {
        return processAsync(this.summarizeConversationFunction, input, kernel);
    }

    private static Mono<String> processAsync(KernelFunction<String> func, String input,
                                             Kernel kernel) {
        List<String> lines = TextChunker.splitPlainTextLines(input, MAX_TOKENS);
        List<String> paragraphs = TextChunker.splitPlainTextParagraphs(lines, MAX_TOKENS);

        return Flux.fromIterable(paragraphs)
                .concatMap(paragraph -> {
                    // The first parameter is the input text.
                    return func.invokeAsync(kernel)
                            .withArguments(
                                    new KernelFunctionArguments.Builder()
                                            .withInput(paragraph)
                                            .build())
                            .withResultType(
                                    ContextVariableTypes.getGlobalVariableTypeForClass(String.class));
                })
                .reduce("", (acc, next) -> {
                    return acc + "\n" + next.getResult();
                });
    }
}

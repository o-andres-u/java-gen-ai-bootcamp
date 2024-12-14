package com.epam.training.gen.ai.configuration;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.epam.training.gen.ai.plugins.ConversationSummaryPlugin;
import com.epam.training.gen.ai.plugins.GenAITrainingConstants;
import com.epam.training.gen.ai.plugins.TimeMachinePlugin;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.plugin.KernelPlugin;
import com.microsoft.semantickernel.plugin.KernelPluginFactory;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class SemanticKernelConfiguration {

    @Bean
    public ChatCompletionService chatCompletionService(@Value("${client.openai.deployment.name}") String deploymentOrModelName,
                                                       OpenAIAsyncClient openAIAsyncClient) {
        return OpenAIChatCompletion.builder()
                .withModelId(deploymentOrModelName)
                .withOpenAIAsyncClient(openAIAsyncClient)
                .build();
    }

    @Bean
    public Kernel kernel(ChatCompletionService chatCompletionService) {
        return Kernel.builder()
                .withAIService(ChatCompletionService.class, chatCompletionService)
                .withPlugin(conversationSummaryPlugin())
                .withPlugin(timeMachinePlugin())
                .build();
    }

    @Bean
    public KernelPlugin conversationSummaryPlugin() {
        return KernelPluginFactory.createFromObject(
                new ConversationSummaryPlugin(),
                GenAITrainingConstants.CONVERSATION_SUMMARY_PLUGIN_NAME
        );
    }

    @Bean
    public KernelPlugin timeMachinePlugin() {
        return KernelPluginFactory.createFromObject(
                new TimeMachinePlugin(),
                GenAITrainingConstants.TIME_MACHINE_PLUGIN_NAME
        );
    }

    @Bean
    public ChatHistory chatHistory() {
        return new ChatHistory();
    }
}

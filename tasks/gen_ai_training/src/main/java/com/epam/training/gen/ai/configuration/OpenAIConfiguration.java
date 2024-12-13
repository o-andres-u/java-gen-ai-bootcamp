package com.epam.training.gen.ai.configuration;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class OpenAIConfiguration {

    @Bean
    public OpenAIAsyncClient openAIAsyncClient(@Value("${client.openai.key}") String azureKey,
                                               @Value("${client.openai.endpoint}") String endpoint) {
        return new OpenAIClientBuilder()
                .credential(new AzureKeyCredential(azureKey))
                .endpoint(endpoint)
                .buildAsyncClient();
    }
}

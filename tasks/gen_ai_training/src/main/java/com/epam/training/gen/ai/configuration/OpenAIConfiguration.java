package com.epam.training.gen.ai.configuration;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAIConfiguration {

    @Value("${client.openai.key}")
    private String azureKey;
    @Value("${client.openai.endpoint}")
    private String endpoint;

    @Bean
    public OpenAIAsyncClient openAIAsyncClient() {
        return new OpenAIClientBuilder()
                .credential(new AzureKeyCredential(azureKey))
                .endpoint(endpoint)
                .buildAsyncClient();
    }
}

package com.epam.training.gen.ai.configuration;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import com.epam.training.gen.ai.model.OpenAIDeployment;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Configuration
@Slf4j
public class OpenAIConfiguration {

    private static final String SUPPORTED_DEPLOYMENTS = "openai/deployments";
    private static final String API_KEY = "Api-Key";

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

    public List<String> getSupportedModels() {
        var httpClient = HttpClient.newHttpClient();
        var httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(endpoint + SUPPORTED_DEPLOYMENTS))
                .header(API_KEY, azureKey)
                .GET()
                .build();
        try {
            var response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            var objectMapper = new ObjectMapper();
            var objects = objectMapper.readValue(response.body(), new TypeReference<List<OpenAIDeployment>>() {});

            return objects.stream()
                    .map(OpenAIDeployment::name)
                    .toList();
        } catch (IOException | InterruptedException e) {
            log.error("Fatal error calling {} endpoint", httpRequest.uri().toString());
            throw new RuntimeException(e.getMessage());
        }
    }
}

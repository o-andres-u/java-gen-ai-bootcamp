package com.epam.training.gen.ai.service;

import com.epam.training.gen.ai.model.OpenAIDeployment;
import com.epam.training.gen.ai.model.OpenAIDeploymentsResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
@Slf4j
public class DialService {

    private static final String API_KEY = "Api-Key";
    private static final String SUPPORTED_DEPLOYMENTS = "openai/deployments";

    @Value("${client.openai.endpoint}")
    private String endpoint;
    @Value("${client.openai.key}")
    private String azureKey;

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
            var responseValue = objectMapper.readValue(response.body(), new TypeReference<OpenAIDeploymentsResponse>() {});

            return responseValue.data().stream()
                    .map(OpenAIDeployment::model)
                    .toList();
        } catch (IOException | InterruptedException e) {
            log.error("Fatal error calling {} endpoint", httpRequest.uri().toString());
            throw new RuntimeException(e);
        }
    }
}

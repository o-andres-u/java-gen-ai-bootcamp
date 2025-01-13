package com.epam.training.gen.ai.service;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.models.EmbeddingItem;
import com.azure.ai.openai.models.Embeddings;
import com.azure.ai.openai.models.EmbeddingsOptions;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class EmbeddingService {

    private static final String EMBEDDING_MODEL_NAME = "text-embedding-ada-002";

    private final OpenAIAsyncClient openAIAsyncClient;

    public String build(String text) {
        var embeddings = buildEmbeddings(text);
        var embeddingStringBuilder = new StringBuilder();
        embeddings.forEach(embeddingItem -> {
            embeddingStringBuilder.append(embeddingItem.getEmbedding());
            embeddingStringBuilder.append("\n");
        });
        return embeddingStringBuilder.toString();
    }

    private List<EmbeddingItem> buildEmbeddings(String text) {
        var embeddings = retrieveEmbeddings(text);
        return Objects.requireNonNull(embeddings.block()).getData();
    }

    private Mono<Embeddings> retrieveEmbeddings(String text) {
        var embeddingsOptions = new EmbeddingsOptions(List.of(text));
        return openAIAsyncClient.getEmbeddings(EMBEDDING_MODEL_NAME, embeddingsOptions);
    }

    public void store(String text) {

    }

    public String search(String text) {
        return null;
    }
}

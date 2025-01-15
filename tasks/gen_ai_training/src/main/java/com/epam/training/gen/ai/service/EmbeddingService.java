package com.epam.training.gen.ai.service;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.models.EmbeddingItem;
import com.azure.ai.openai.models.Embeddings;
import com.azure.ai.openai.models.EmbeddingsOptions;
import io.qdrant.client.QdrantClient;
import io.qdrant.client.grpc.Collections;
import io.qdrant.client.grpc.Points;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import static io.qdrant.client.PointIdFactory.id;
import static io.qdrant.client.ValueFactory.value;
import static io.qdrant.client.VectorsFactory.vectors;

@Service
@Slf4j
public class EmbeddingService {

    private static final String COLLECTION_NAME = "gen_ai_course";
    private static final String EMBEDDING_MODEL_NAME = "text-embedding-ada-002";

    private final OpenAIAsyncClient openAIAsyncClient;
    private final QdrantClient qdrantClient;

    public EmbeddingService(OpenAIAsyncClient openAIAsyncClient, QdrantClient qdrantClient) throws ExecutionException, InterruptedException {
        this.openAIAsyncClient = openAIAsyncClient;
        this.qdrantClient = qdrantClient;
        createCollection();
    }

    private void createCollection() throws ExecutionException, InterruptedException {
        var result = qdrantClient.createCollectionAsync(COLLECTION_NAME,
                Collections.VectorParams.newBuilder()
                        .setDistance(Collections.Distance.Cosine)
                        .setSize(1536)
                        .build()
            ).get();

        log.info("Collection was created: [{}]", result.getResult());
    }

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

    public void store(String text) throws ExecutionException, InterruptedException {
        var embeddings = buildEmbeddings(text);
        save(embeddings);
    }

    private void save(List<EmbeddingItem> embeddings) throws ExecutionException, InterruptedException {
        var points = new ArrayList<List<Float>>();
        embeddings.forEach(embeddingItem -> {
            var values = new ArrayList<>(embeddingItem.getEmbedding());
            points.add(values);
        });

        var pointStructs = new ArrayList<Points.PointStruct>();
        points.forEach(point -> {
            var pointStruct = buildPointStruct(point);
            pointStructs.add(pointStruct);
        });

        saveVector(pointStructs);
    }

    private Points.PointStruct buildPointStruct(List<Float> point) {
        return Points.PointStruct.newBuilder()
                .setId(id(1))
                .setVectors(vectors(point))
                .putAllPayload(Map.of("info", value("Some info")))
                .build();
    }

    private void saveVector(List<Points.PointStruct> pointStructs) throws ExecutionException, InterruptedException {
        var result = qdrantClient.upsertAsync(COLLECTION_NAME, pointStructs).get();
        log.info(result.getStatus().name());
    }

    public String search(String text) {
        return null;
    }
}

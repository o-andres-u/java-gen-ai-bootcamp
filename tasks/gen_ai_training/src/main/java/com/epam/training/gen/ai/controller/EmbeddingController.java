package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.model.EmbeddingRequest;
import com.epam.training.gen.ai.service.EmbeddingService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
@AllArgsConstructor
public class EmbeddingController {

    private EmbeddingService embeddingService;

    @PostMapping("embedding/build")
    public String buildFromText(@RequestBody EmbeddingRequest request) {
        return embeddingService.build(request.text());
    }

    @PostMapping("embedding/store")
    public void buildAndStoreFromText(@RequestBody EmbeddingRequest request) throws ExecutionException, InterruptedException {
        embeddingService.store(request.text());
    }

    @GetMapping("embedding/search")
    public String searchForClosest(@RequestParam String text) throws ExecutionException, InterruptedException {
        return embeddingService.search(text);
    }
}

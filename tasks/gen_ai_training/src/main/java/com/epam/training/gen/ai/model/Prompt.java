package com.epam.training.gen.ai.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Prompt (
        String input,
        String model,
        double temperature,
        @JsonProperty("max-tokens") int maxTokens) { }

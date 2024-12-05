package com.epam.training.gen.ai.model;

import java.util.List;

public record OpenAIDeploymentsResponse(List<OpenAIDeployment> data, String object) { }

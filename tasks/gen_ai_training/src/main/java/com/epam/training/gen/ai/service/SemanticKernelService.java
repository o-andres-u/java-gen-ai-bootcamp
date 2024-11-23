package com.epam.training.gen.ai.service;

import com.microsoft.semantickernel.Kernel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SemanticKernelService {

    private final Kernel kernel;

    public String processPrompt(String prompt) {
        return null;
    }
}

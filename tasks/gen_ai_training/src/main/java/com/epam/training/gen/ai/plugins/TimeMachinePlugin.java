package com.epam.training.gen.ai.plugins;

import com.microsoft.semantickernel.semanticfunctions.annotations.DefineKernelFunction;
import com.microsoft.semantickernel.semanticfunctions.annotations.KernelFunctionParameter;

import java.util.ArrayList;
import java.util.List;

public class TimeMachinePlugin {

    private final List<Integer> visitedYears;

    public TimeMachinePlugin() {
        visitedYears = new ArrayList<>();
    }

    @DefineKernelFunction(
            name = "travel",
            description = "Executes the time machin to travel to a specific city and year",
            returnDescription = "Returns a message telling where the time traveler is",
            returnType = "java.lang.String"
    )
    public String travel(
            @KernelFunctionParameter(name = "city", description = "The city the traveler wants to go to") String city,
            @KernelFunctionParameter(name = "year", description = "The year the traveler wants to go to") int year) {
        visitedYears.add(year);
        return String.format("The time machine started traveling to %s in %d", city, year);
    }

    @DefineKernelFunction(
            name = "get_visited_years",
            description = "Gets the list of years that the traveler has visited",
            returnType = "java.util.List"
    )
    public List<Integer> getVisitedYears() {
        return visitedYears;
    }
}

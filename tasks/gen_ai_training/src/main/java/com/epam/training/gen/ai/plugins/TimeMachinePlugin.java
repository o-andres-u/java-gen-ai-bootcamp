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
            name = "travel_in_time",
            description = "Function to be used when the user wants to travel in time.",
            returnDescription = "Returns a message telling where the time traveler is",
            returnType = "java.lang.String"
    )
    public String travel_in_time(
            @KernelFunctionParameter(name = "city", description = "The city in which the traveler wants to travel in time to") String city,
            @KernelFunctionParameter(name = "year", description = "The year in which the traveler wants to travel in time to") int year) {
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

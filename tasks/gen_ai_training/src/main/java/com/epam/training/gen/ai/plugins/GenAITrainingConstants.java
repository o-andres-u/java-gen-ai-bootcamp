package com.epam.training.gen.ai.plugins;

public class GenAITrainingConstants {

    public static final String SummarizeConversationDefinition = """
        BEGIN CONTENT TO SUMMARIZE:
        {{$INPUT}}

        END CONTENT TO SUMMARIZE.

        Summarize the conversation in 'CONTENT TO SUMMARIZE', identifying main points of discussion and any conclusions that were reached.
        Do not incorporate other general knowledge.
        Summary is in plain text, in complete sentences, with no markup or tags.

        BEGIN SUMMARY:
        """
        .stripIndent();

    public static final String SUMMARIZE_CONVERSATION_FUNCTION_NAME = "summarize_conversation";
    public static final String CONVERSATION_SUMMARY_PLUGIN_NAME = "ConversationSummaryPlugin";

    public static final String TIME_MACHINE_PLUGIN_NAME = "TimeMachinePlugin";
}

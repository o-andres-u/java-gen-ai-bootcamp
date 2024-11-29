package com.epam.training.gen.ai.plugins;

public class PromptFunctionConstants {

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

    public static final String SUMMARIZE_CONVERSATION = "SummarizeConversation";
    public static final String CONVERSATION_SUMMARY_PLUGIN = "ConversationSummaryPlugin";
}

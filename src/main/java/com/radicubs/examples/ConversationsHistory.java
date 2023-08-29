package com.radicubs.examples;

import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import com.slack.api.model.Message;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

public class ConversationsHistory {

    static Optional<List<Message>> conversationHistory = Optional.empty();

    /**
     * Fetch conversation history using ID from last example
     */
    static void fetchHistory(String id) {
        // you can get this instance via ctx.client() in a Bolt app
        var client = Slack.getInstance().methods();
        var logger = LoggerFactory.getLogger("my-awesome-slack-app");
        try {
            // Call the conversations.history method using the built-in WebClient
            var result = client.conversationsHistory(r -> r
                    // The token you used to initialize your app
                    .token("xoxb-5716940559697-5735533709972-Ln0h3OYtfPsQ5r2gbBSB1eJ9")
                    .channel(id)
            );
            conversationHistory = Optional.ofNullable(result.getMessages());
            // Print results
            logger.info("{} messages found in {}", conversationHistory.orElse(emptyList()).size(), id);
        } catch (IOException | SlackApiException e) {
            logger.error("error: {}", e.getMessage(), e);
        }
    }

    public static void main(String[] args) throws Exception {
        fetchHistory("C05LHK7SK54");
    }

}

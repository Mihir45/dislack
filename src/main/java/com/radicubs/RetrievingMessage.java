import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class RetrievingMessage {

    /**
     * Fetch conversation history using the ID and a TS from the last example
     */
    static void fetchMessage(String id, String ts) {
        // you can get this instance via ctx.client() in a Bolt app
        var client = Slack.getInstance().methods();
        var logger = LoggerFactory.getLogger("my-awesome-slack-app");
        try {
            // Call the chat.postMessage method using the built-in WebClient
            var result = client.conversationsHistory(r -> r
                    // The token you used to initialize your app
                    .token("xoxb-5716940559697-5735533709972-Ln0h3OYtfPsQ5r2gbBSB1eJ9")
                    .channel(id)
                    // In a more realistic app, you may store ts data in a db
                    .latest(ts)
                    // Limit results
                    .inclusive(true)
                    .limit(1)
            );
            // There should only be one result (stored in the zeroth index)
            var message = result.getMessages().get(0);
            // Print message text
            logger.info("result {}", message.getText());
        } catch (IOException | SlackApiException e) {
            logger.error("error: {}", e.getMessage(), e);
        }
    }

    public static void main(String[] args) throws Exception {
        // Fetch message using a channel ID and message TS
        fetchMessage("C05LHK7SK54", "");
    }

}

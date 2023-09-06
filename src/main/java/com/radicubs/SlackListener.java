package com.radicubs;
import com.slack.api.Slack;
import com.slack.api.bolt.App;
import com.slack.api.bolt.AppConfig;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.model.event.AppMentionEvent;
import com.slack.api.bolt.socket_mode.SocketModeApp;
import com.slack.api.model.event.MessageEvent;

import javax.xml.namespace.QName;
import java.io.IOException;

public class SlackListener {
    private static App app;
    private static MethodsClient client;
    private static final String APP_TOKEN = "xapp-1-A05ME11337A-5733045732739-f933fb4cfaf9fd1a7a2e4556718e38c76fc6d282ffb8b854c6b9f1edcbc6dddd";
    private static final String BOT_TOKEN = "xoxb-5716940559697-5735533709972-Ln0h3OYtfPsQ5r2gbBSB1eJ9";
    public static void main(String[] args) throws Exception {

        client = Slack.getInstance().methods();
        AppConfig appConfig = AppConfig.builder().singleTeamBotToken(BOT_TOKEN).build();

        app = new App(appConfig);

        app.event(MessageEvent.class, (req, ctx) -> {
            DiscordListener.sendDiscordMessage(req.getEvent().getText());
            ctx.say("Hi there!");
            return ctx.ack();
        });

        SocketModeApp socketModeApp = new SocketModeApp(APP_TOKEN, app);
        socketModeApp.start();
    }
    public static void sendSlackMessage(String content, String name, String url) throws SlackApiException, IOException {
        ChatPostMessageResponse result = client.chatPostMessage(r -> r
                        // The token you used to initialize your app
                        .token(BOT_TOKEN)
                        .channel("C05LHK7SK54")
                        .text(content)
                        .username(name)
                        .iconUrl(url)

                // You could also use a blocks[] array to send richer content
        );
    }


}

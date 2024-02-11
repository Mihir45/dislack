package com.radicubs;
import com.slack.api.Slack;
import com.slack.api.bolt.App;
import com.slack.api.bolt.AppConfig;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.methods.response.users.UsersIdentityResponse;
import com.slack.api.methods.response.users.profile.UsersProfileGetResponse;
import com.slack.api.model.User;
import com.slack.api.model.event.AppMentionEvent;
import com.slack.api.bolt.socket_mode.SocketModeApp;
import com.slack.api.model.event.MessageEvent;

import javax.xml.namespace.QName;
import java.io.IOException;

public class SlackListener {
    private static App app;
    private static MethodsClient client;
    private static final String APP_TOKEN = "SLACK APP TOKEN";
    private static final String BOT_TOKEN = "SLACK BOT TOKEN";
    public static void main(String[] args) throws Exception {

        AppConfig appConfig = AppConfig.builder().singleTeamBotToken(BOT_TOKEN).build();

        app = new App(appConfig);

        app.event(MessageEvent.class, (req, ctx) -> {
            String slackUserId = req.getEvent().getUser();
            UsersProfileGetResponse result = client.usersProfileGet(r -> r
                    .token(BOT_TOKEN)
                    .user(slackUserId));

            User.Profile profile = result.getProfile();

            DiscordListener.sendDiscordMessage(req.getEvent().getText(), profile.getDisplayNameNormalized(), slackUserId, profile.getImage192());
            return ctx.ack();
        });

        client = app.getClient();

        SocketModeApp socketModeApp = new SocketModeApp(APP_TOKEN, app);
        socketModeApp.start();
    }
    public static void sendSlackMessage(String content, String name, String url) throws SlackApiException, IOException {
        ChatPostMessageResponse result = client.chatPostMessage(r -> r
                        .token(BOT_TOKEN)
                        .channel("C05LHK7SK54")
                        .text(content)
                        .username(name)
                        .iconUrl(url)

        );
    }
}

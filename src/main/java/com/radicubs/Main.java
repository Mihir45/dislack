package com.radicubs;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class Main {
    static final String BOT_TOKEN = "MTE0MDAxOTE0MDA5NTEzNTc3NQ.GBrK9k.mm689iw4ieC5WDZPTNYiexOhYYz9MbhygVstz8";
    public static void main(String[] arguments) throws Exception {
        JDA api = JDABuilder.createDefault(BOT_TOKEN, GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS).build();
        api.addEventListener(new DiscordListener());
    }
}

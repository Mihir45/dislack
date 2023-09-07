package com.radicubs;

import com.slack.api.methods.SlackApiException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.io.IOException;
import java.util.Objects;

public class DiscordListener extends ListenerAdapter
{
    private static JDA api;
    private static final String BOT_TOKEN = "MTE0MDAxOTE0MDA5NTEzNTc3NQ.GBrK9k.mm689iw4ieC5WDZPTNYiexOhYYz9MbhygVstz8";
    public static void main(String[] arguments){
        api = JDABuilder.createDefault(BOT_TOKEN, GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS).build();
        api.addEventListener(new DiscordListener());
        api.upsertCommand("setup", "sets up the discord server with the slack stuff").queue();
    }
    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        if (event.getAuthor().isBot()) return;
        Message message = event.getMessage();
        String content = message.getContentRaw();
        String name = event.getMember().getNickname();
        if(name == null)
            name = event.getMember().getEffectiveName();
        try {

            SlackListener.sendSlackMessage(content, name, event.getMember().getEffectiveAvatarUrl());
        } catch (SlackApiException | IOException e) {
            System.out.println("failed to send message");
        }
        if (content.equals("!ping"))
        {
            MessageChannel channel = event.getChannel();
            channel.sendMessage("Pong!").queue(); // Important to call .queue() on the RestAction returned by sendMessage(...)
        }
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if(event.getName().equals("setup")){
            //event.deferReply().queue();
            event.reply("Shhhh! This is a secret! Karthik is a nerd!").queue();
        }
    }

    public static void sendDiscordMessage(String content) {
        long channelId = 1138202654540054631L;
        TextChannel channel = api.getTextChannelById(channelId);
        if (Objects.equals(content, "<@U05L9NTP223>")) {
            channel.sendMessage("<@580538439003537418>").queue();
        } else if (Objects.equals(content, "<@U05LQ5V7XHR>")) {
            channel.sendMessage("<@692151503061778492>").queue();
        } else {
            channel.sendMessage(content).queue();
        }
    }

}
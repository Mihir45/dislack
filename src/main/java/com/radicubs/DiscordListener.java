package com.radicubs;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import com.slack.api.methods.SlackApiException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Icon;
import net.dv8tion.jda.api.entities.Message;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.WebhookManager;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.WebhookAction;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
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
        switch (content){
            case "<@580538439003537418>":
                content = "<@U05L9NTP223>";
                break;
            case "<@692151503061778492>":
                content = "<@U05LQ5V7XHR>";
                break;

        }
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

    public static void sendDiscordMessage(String content, String name, String url){
        System.out.println(name + " " + url);
        long channelId = 1138202654540054631L;
        TextChannel channel = api.getTextChannelById(channelId);

        channel.createWebhook(name).queue(webhook -> {
            try{
                WebhookManager manager = webhook.getManager();
                manager.setAvatar(Icon.from(new URL(url).openStream(), Icon.IconType.PNG)).queue();
            }
            catch(IOException e){
                throw new RuntimeException(e.getMessage());
            }
            WebhookClient client = WebhookClientBuilder.fromJDA(webhook).build();

            if (content.equals("<@U05L9NTP223>")) {
                client.send("<@580538439003537418>");
            } else if (content.equals("<@U05LQ5V7XHR>")) {
                client.send("<@692151503061778492>");
            } else {
                client.send(content);
            }
            webhook.delete().queue();
        });

    }

}
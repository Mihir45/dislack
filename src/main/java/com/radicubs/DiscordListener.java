package com.radicubs;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import com.slack.api.methods.SlackApiException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Icon;
import net.dv8tion.jda.api.entities.Message;

import net.dv8tion.jda.api.entities.Webhook;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.WebhookManager;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.WebhookAction;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.Consumer;

public class DiscordListener extends ListenerAdapter
{
    private static JDA api;
    private static final String BOT_TOKEN = "DISCORD BOT TOKEN";
    private static HashMap<String, String> slackIdToWebhook = new HashMap<>();
    private static Scanner f;
    private static FileWriter fw;
    public static void main(String[] arguments) throws IOException{
        f = new Scanner(new File("SlackIdToWebhook.txt"));
        fw = new FileWriter("SlackIdToWebhook.txt", true);
        while(f.hasNext()){
            String[] ids = f.nextLine().split(" ");
            slackIdToWebhook.put(ids[0], ids[1]);
        }

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
            channel.sendMessage("Pong!").queue();
        }
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if(event.getName().equals("setup")){
            event.reply("Setup command!").queue();
        }
    }

    public static void sendDiscordMessage(String content, String name, String id, String url){
        System.out.println(name + " " + url);
        long channelId = 1138202654540054631L;
        TextChannel channel = api.getTextChannelById(channelId);


        Consumer onWebhook = w -> {
            Webhook webhook = (Webhook) w;
            try{
                WebhookManager manager = webhook.getManager();
                manager.setAvatar(Icon.from(new URL(url).openStream(), Icon.IconType.PNG)).queue();
            }
            catch(IOException e){
                throw new RuntimeException(e);
            }

            WebhookClient client = WebhookClientBuilder.fromJDA(webhook).build();

            client.send(content);

            if(!slackIdToWebhook.containsKey(id)){
                try {
                    fw.write("" + id + " " + webhook.getId() + "\n");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                slackIdToWebhook.put(id, webhook.getId());
            }
        };

        boolean failed = true;
        if(slackIdToWebhook.containsKey(id)){
            try{
                api.retrieveWebhookById(slackIdToWebhook.get(id)).queue(onWebhook);
                failed = false;
            }
            catch(Exception e){
                System.out.println("Retrieving existed webhook errored out :/");
            }
        }
        if(failed){
            channel.createWebhook(name).queue(onWebhook);
        }
    }

}
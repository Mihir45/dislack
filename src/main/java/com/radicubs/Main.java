package com.radicubs;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class Main {
    static final String BOT_TOKEN = "MTE0MDAxOTE0MDA5NTEzNTc3NQ.G4F0nF.Zf-St1GkihbgaVTVeXt0NrYJvZvssRWcjC6g6E";
    public static void main(String[] arguments) throws Exception
    {
        JDA api = JDABuilder.createDefault(BOT_TOKEN).build();
        api.addEventListener(new DiscordListener());
    }
}

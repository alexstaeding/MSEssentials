package essentials.discordbridge;

import essentials.MSEssentials;
import essentials.discordbridge.discord.ConnectionListener;
import essentials.discordbridge.velocity.DiscordStaffChat;
import essentials.discordbridge.discord.MSEssentialsChatListener;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import static java.lang.System.in;

public class Bridge {

    private static DiscordApi discordApi;
    private static DiscordConfig config;


    public static void enable(){
        MSEssentials.logger.info("Enabling Discord Bridge!");

        try{
            DiscordConfig.enable();
        }catch (Exception e)
        {
            throw new RuntimeException("Failed to load config", e);
        }


        startBot();


    }

    public static void onProxyShutdown() {
        MSEssentials.getServer().getScheduler().buildTask(MSEssentials.instance, () -> {
            MSEssentials.logger.info("Disconnecting the bridge");
            discordApi.disconnect();
            MSEssentials.logger.info("Successfully disconnected!");
        }).schedule();
    }

    private Bridge(DiscordApi dAPI, DiscordConfig configuration){
        config = configuration;
        discordApi = dAPI;
    }

    public static boolean reloadConfig(){
        final String oldToken = config.getToken();
        try
        {
            DiscordConfig.enable();
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    private static void startBot() {
        if(discordApi != null)
        {
            discordApi.disconnect();
            discordApi = null;
        }

        ConnectionListener connectionListener = new ConnectionListener();
        DiscordStaffChat discordStaffChat = new DiscordStaffChat();
        MSEssentialsChatListener chatListener = new MSEssentialsChatListener();

        new DiscordApiBuilder()
                .setToken(config.getToken())
                .addLostConnectionListener(connectionListener::onConnectionLost)
                .addReconnectListener(connectionListener::onReconnect)
                .addResumeListener(connectionListener::onResume)
               // .addMessageCreateListener(discordStaffChat::onMessage)
                .addMessageCreateListener(chatListener::onMessage)
                .login().thenAccept(discordApi1 ->
        {
            discordApi = discordApi1;
            MSEssentials.logger.info("Connected to discord");
        });
    }

    public static DiscordConfig getConfig()
    {
        return config;
    }

    public static DiscordApi getDiscordApi() {
        return discordApi;
    }
}

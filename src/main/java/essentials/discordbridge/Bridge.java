package essentials.discordbridge;

import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import essentials.MSEssentials;
import essentials.discordbridge.discord.ConnectionListener;
import essentials.discordbridge.velocity.DiscordStaffChat;
import essentials.discordbridge.discord.MSEssentialsChatListener;
import essentials.modules.Config.ConfigKeys;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.TextChannel;
import rocks.milspecsg.msrepository.api.config.ConfigurationService;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.System.in;

@Singleton
public class Bridge {

    private DiscordApi discordApi;

    ConfigurationService configurationService;


    @Inject
    public Bridge(@Named("discord") ConfigurationService configurationService) {
        this.configurationService = configurationService;
        configurationService.addConfigLoadedListener(this::configLoaded);
    }

    public void onProxyShutdown() {
        MSEssentials.getServer().getScheduler().buildTask(MSEssentials.instance, () -> {
            MSEssentials.logger.info("Disconnecting the bridge");
            discordApi.disconnect();
            MSEssentials.logger.info("Successfully disconnected!");
        }).schedule();
    }

//    public static boolean reloadConfig(){
//        final String oldToken = config.getToken();
//        try
//        {
//            config = loadConfig();
//        }catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//        return true;
//    }

//    private static MSDBConfig loadConfig() throws Exception
//    {
//        ConfigurationNode config = YAMLConfigurationLoader.builder()
//                .setFile(getBundledFile("discord.conf"))
//                .build()
//                .load();
//        return new MSDBConfig(config);
//    }

//    private static File getBundledFile(String name)
//    {
//        File file = new File(MSEssentials.defaultConfigPath.toFile(), name);
//
//        if(!file.exists())
//        {
//            MSEssentials.defaultConfigPath.toFile().mkdir();
//            try(InputStream n = MSEssentials.class.getResourceAsStream("/" + name))
//            {
//                Files.copy(in, file.toPath());
//            }catch (IOException e)
//            {
//                e.printStackTrace();
//            }
//        }
//        return file;
//    }

    private boolean alreadyLoaded = false;

    private void configLoaded(Object plugin) {
        if (!alreadyLoaded) {
            MSEssentials.logger.info("Starting discord bridge");
            startBot();
            alreadyLoaded = true;
        }
    }

    private void startBot() {
        if(discordApi != null)
        {
            discordApi.disconnect();
            discordApi = null;
        }

        ConnectionListener connectionListener = new ConnectionListener();
        DiscordStaffChat discordStaffChat = new DiscordStaffChat();
        MSEssentialsChatListener chatListener = new MSEssentialsChatListener();

        String token = configurationService.getConfigString(ConfigKeys.DISCORD_TOKEN);
        if (token.equals("replace")) {
            System.err.println("Discord token not set!");
            return;
        }

        new DiscordApiBuilder()
                .setToken(token)
                .addLostConnectionListener(connectionListener::onConnectionLost)
                .addReconnectListener(connectionListener::onReconnect)
                .addResumeListener(connectionListener::onResume)
//                .addMessageCreateListener(discordStaffChat::onMessage)
                .addMessageCreateListener(chatListener::onMessage)
                .login().thenAccept(discordApi1 ->
        {
            discordApi = discordApi1;
            MSEssentials.logger.info("Connected to discord");
        });
    }

    private List<TextChannel> getChannel(DiscordApi api, List<Long> channels) {
        return channels.stream()
            .map(api::getTextChannelById)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
    }

    public List<TextChannel> getInChannels(DiscordApi api)
    {
        return getChannel(api, configurationService.getConfigList(ConfigKeys.DISCORD_IN_CHANNELS, new TypeToken<List<Long>> () {}));
    }

    public List<TextChannel> getOutChannels(DiscordApi api)
    {
        return getChannel(api, configurationService.getConfigList(ConfigKeys.DISCORD_OUT_CHANNELS, new TypeToken<List<Long>> () {}));
    }

    public List<TextChannel> getStaffChannel(DiscordApi api)
    {
        return getChannel(api, configurationService.getConfigList(ConfigKeys.DISCORD_STAFF_CHANNELS, new TypeToken<List<Long>> () {}));
    }

    public DiscordApi getDiscordApi() {
        return discordApi;
    }
}

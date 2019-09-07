package essentials;

import com.google.common.io.ByteArrayDataInput;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.LegacyChannelIdentifier;
import essentials.discordbridge.Bridge;
import essentials.discordbridge.discord.MSEssentialsChatListener;
import essentials.discordbridge.velocity.*;
import essentials.modules.Config.ConfigKeys;
import essentials.modules.Config.MSConfigurationService;
import essentials.modules.Config.MSDiscordConfigurationService;
import essentials.modules.StaffChat.StaffChat;
import essentials.modules.StaffChat.StaffChatEvent;
import essentials.modules.commands.*;
import essentials.modules.proxychat.ProxyChatEvent;
import essentials.modules.tab.ConfigManager;
import essentials.modules.tab.GlobalTab;
import essentials.modules.tab.TabPlayerLeave;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import me.lucko.luckperms.*;
import me.lucko.luckperms.api.*;
import org.spongepowered.api.data.key.Keys;
import rocks.milspecsg.msrepository.APIConfigurationModule;
import rocks.milspecsg.msrepository.api.config.ConfigurationService;
import rocks.milspecsg.msrepository.service.config.ApiConfigurationService;

import javax.inject.Inject;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;


@Plugin(description = "your one stop velocity plugin!",
    authors = "STG_Allen",
    version = "1.0",
    id = "msessentials",
    dependencies = {
        @Dependency(id = "luckperms")
    })
public class MSEssentials {

    public static ProxyServer server;
    public static Logger logger;
    public static Path defaultConfigPath;

    public static LuckPermsApi api;

//    public static WordCatch wordCatch;
//     MSLangConfig msLangConfig;

    public static MSEssentials instance = null;
    public static Map<String, Double> playerBalances = new HashMap<String, Double>();

    @Inject
    Injector velocityRootInjector;

    private Injector injector = null;


    @Subscribe
    public void onShutdown(ProxyShutdownEvent e) {
        injector.getInstance(Bridge.class).onProxyShutdown();
    }

    @Subscribe
    public void onInit(ProxyInitializeEvent event) {
        // needs to be first thing
        initServices();
        logger.info("is now starting!");
//        this.msLangConfig = new MSLangConfig(this);
//        this.wordCatch = new WordCatch(this, server);
        server.getChannelRegistrar().register(new LegacyChannelIdentifier("GlobalTab"));

        // command loading moved here
        injector.getInstance(MSEssentialsCommandManager.class).register();

        logger.info("enabling configs");
//        MSLangConfig.enable();
//        PlayerConfig.enable();
//        MSEssentialsConfig.enable();
        ConfigManager.setupConfig();


        instance = this;

        // enable is called after config is loaded
        injector.getInstance(Bridge.class);

        StaffChat.toggledSet = new HashSet<UUID>();

        if (server.getPluginManager().isLoaded("luckperms")) {
            reload();
        }
        logger.info("Enabling GlobalTab");
        GlobalTab.schedule();

        injector.getInstance(Key.get(ConfigurationService.class, Names.named("msessentials"))).addConfigLoadedListener(plugin -> {
            if (!alreadyLoaded) {
                initListeners();
                alreadyLoaded = true;
            }
        });

        // this must be last to run on plugin init
        injector.getInstance(Key.get(ConfigurationService.class, Names.named("msessentials"))).load(this);
        injector.getInstance(Key.get(ConfigurationService.class, Names.named("discord"))).load(this);
    }

    public void reload() {
        api = LuckPerms.getApi();
        logger.info("luckperms api connected successfully.");
    }

    @Inject
    public MSEssentials(ProxyServer pserver, Logger log, @DataDirectory Path path) {
        defaultConfigPath = path;
        logger = log;
        server = pserver;
    }

    public static Logger getLogger() {
        return logger;
    }

//    public MSLangConfig getMSLangConfig()
//    {
//        return msLangConfig;
//    }


    public static ProxyServer getServer() {
        return server;
    }

    private boolean alreadyLoaded = false;

    public void initListeners() {
        logger.info("initializing listeners");

        if (injector.getInstance(Key.get(ConfigurationService.class, Names.named("msessentials"))).getConfigBoolean(ConfigKeys.PROXY_CHAT_ENABLED)) {
            server.getEventManager().register(this, injector.getInstance(ProxyChatListener.class));
        }

        server.getEventManager().register(this, injector.getInstance(StaffChatEvent.class));
        server.getEventManager().register(this, injector.getInstance(MSEssentialsChatListener.class));
        server.getEventManager().register(this, injector.getInstance(VelocityListener.class));
        //server.getEventManager().register(this, new StaffChatListener());
        // server.getEventManager().register(this, new DiscordStaffChat());
        server.getEventManager().register(this, injector.getInstance(ProxyChatEvent.class));
        server.getEventManager().register(this, injector.getInstance(TabPlayerLeave.class));
    }

    @Subscribe
    public void onPluginMessage(PluginMessageEvent event) {
        if (!event.getIdentifier().equals(new LegacyChannelIdentifier("GlobalTab"))) {
            return;
        }

        event.setResult(PluginMessageEvent.ForwardResult.handled());

        if (!(event.getSource() instanceof ServerConnection)) {
            return;
        }

        ByteArrayDataInput in = event.dataAsDataStream();
        String subChannel = in.readUTF();

        if (subChannel.equals("Balance")) {
            String[] packet = in.readUTF().split(":");
            String username = packet[0];
            Double balance = Double.parseDouble(packet[1]);
            if (playerBalances.containsKey(username))
                playerBalances.replace(username, balance);
            else
                playerBalances.put(username, balance);
        }
    }

    private void initServices() {
        injector = velocityRootInjector.createChildInjector(new MSEssentialsConfigurationModule(), new MSEssentialsModule());
    }


    private static class MSEssentialsConfigurationModule extends AbstractModule {
        @Override
        protected void configure() {

            bind(new TypeLiteral<ConfigurationService>() {})
                .annotatedWith(Names.named("msessentials"))
                .to(new TypeLiteral<MSConfigurationService>() {});

            bind(new TypeLiteral<ConfigurationService>() {})
                .annotatedWith(Names.named("discord"))
                .to(new TypeLiteral<MSDiscordConfigurationService>() {});

            bind(new TypeLiteral<ConfigurationLoader<CommentedConfigurationNode>>() {}).annotatedWith(Names.named("msessentials"))
                .toInstance(getConfig("msessentials"));

            bind(new TypeLiteral<ConfigurationLoader<CommentedConfigurationNode>>() {}).annotatedWith(Names.named("discord"))
                .toInstance(getConfig("discord"));
        }
    }
    public static ConfigurationLoader<CommentedConfigurationNode> getConfig(String name) {
        Path configPath = Paths.get(MSEssentials.defaultConfigPath + "/" + name + ".conf");
        try {
            if (!Files.exists(configPath)) {
                Files.createDirectories(MSEssentials.defaultConfigPath);
                Files.createFile(configPath);
            }
            return HoconConfigurationLoader.builder()
                .setPath(configPath)
                .build();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private static class MSEssentialsModule extends AbstractModule {
        @Override
        protected void configure() {
            // other bindings go in here
        }
    }
}

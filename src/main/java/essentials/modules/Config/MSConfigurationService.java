package essentials.modules.Config;

import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import rocks.milspecsg.msrepository.service.config.ApiConfigurationService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MSConfigurationService extends ApiConfigurationService {

    @Inject
    public MSConfigurationService(ConfigurationLoader<CommentedConfigurationNode> configLoader) {
        super(configLoader);
    }

    @Override
    protected void initNodeTypeMap() {
        nodeTypeMap.put(ConfigKeys.PROXY_CHAT_ENABLED, new TypeToken<Boolean>() {});
        nodeTypeMap.put(ConfigKeys.CHAT_FILTER_ENABLED, new TypeToken<Boolean>() {});
        nodeTypeMap.put(ConfigKeys.DISCORD_BRIDGE_ENABLED, new TypeToken<Boolean>() {});
        nodeTypeMap.put(ConfigKeys.SWEARS_LIST, new TypeToken<List<String>>() {});
        nodeTypeMap.put(ConfigKeys.SWEARS_EXCEPTION_LIST, new TypeToken<List<String>>() {});
        nodeTypeMap.put(ConfigKeys.SWEAR_REPLACEMENT, new TypeToken<String>() {});
    }

    @Override
    protected void initVerificationMaps() {

    }

    @Override
    protected void initDefaultMaps() {
        defaultBooleanMap.put(ConfigKeys.PROXY_CHAT_ENABLED, true);
        defaultBooleanMap.put(ConfigKeys.CHAT_FILTER_ENABLED, true);
        defaultBooleanMap.put(ConfigKeys.DISCORD_BRIDGE_ENABLED, true);
        defaultListMap.put(ConfigKeys.SWEARS_LIST, Collections.emptyList());
        defaultListMap.put(ConfigKeys.SWEARS_EXCEPTION_LIST, Collections.emptyList());
        defaultStringMap.put(ConfigKeys.SWEAR_REPLACEMENT, "I <3 MilspecSG!");
    }

    @Override
    protected void initNodeNameMap() {

        nodeNameMap.put(ConfigKeys.PROXY_CHAT_ENABLED, "proxyChatEnabled");
        nodeNameMap.put(ConfigKeys.CHAT_FILTER_ENABLED, "chatFilterEnabled");
        nodeNameMap.put(ConfigKeys.DISCORD_BRIDGE_ENABLED, "discordBridgeEnabled");
        nodeNameMap.put(ConfigKeys.SWEARS_LIST, "swears");
        nodeNameMap.put(ConfigKeys.SWEARS_EXCEPTION_LIST, "exceptions");
        nodeNameMap.put(ConfigKeys.SWEAR_REPLACEMENT, "swearReplacement");

    }

    @Override
    protected void initNodeDescriptionMap() {
        nodeDescriptionMap.put(ConfigKeys.PROXY_CHAT_ENABLED, "\nWhether proxy chat is enabled. true/false");
        nodeDescriptionMap.put(ConfigKeys.CHAT_FILTER_ENABLED, "\nWhether chat filter is enabled. true/false");
        nodeDescriptionMap.put(ConfigKeys.DISCORD_BRIDGE_ENABLED, "\nWhether discord bridge is enabled. true/false");
        nodeDescriptionMap.put(ConfigKeys.SWEARS_LIST, "\nAdd swear words to be censored with /language add [word]");
        nodeDescriptionMap.put(ConfigKeys.SWEARS_EXCEPTION_LIST, "\nAdd swear words to be exempted with /language exempt [word]");
        nodeDescriptionMap.put(ConfigKeys.SWEAR_REPLACEMENT, "\nMessage to replace messages containing swear words.");


    }
}

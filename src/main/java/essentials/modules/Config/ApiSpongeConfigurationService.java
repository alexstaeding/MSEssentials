package essentials.modules.Config;

import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import rocks.milspecsg.msrepository.service.config.ApiConfigurationService;

import java.util.List;

public class ApiSpongeConfigurationService extends ApiConfigurationService {

    @Inject
    public ApiSpongeConfigurationService(ConfigurationLoader<CommentedConfigurationNode> configLoader) {
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

    }

    @Override
    protected void initNodeNameMap() {

    }

    @Override
    protected void initNodeDescriptionMap() {

    }
}

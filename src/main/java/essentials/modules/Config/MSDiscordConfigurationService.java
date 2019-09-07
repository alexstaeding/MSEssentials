package essentials.modules.Config;

import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import rocks.milspecsg.msrepository.service.config.ApiConfigurationService;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

@Singleton
public class MSDiscordConfigurationService extends ApiConfigurationService {

    @Inject
    public MSDiscordConfigurationService(@Named("discord") ConfigurationLoader<CommentedConfigurationNode> configLoader) {
        super(configLoader);
    }

    @Override
    protected void initNodeTypeMap() {
        TypeToken<List<Long>> longListTypeToken = new TypeToken<List<Long>> () {};

        nodeTypeMap.put(ConfigKeys.DISCORD_TOKEN, TypeToken.of(String.class));
        nodeTypeMap.put(ConfigKeys.DISCORD_IN_CHANNELS, longListTypeToken);
        nodeTypeMap.put(ConfigKeys.DISCORD_OUT_CHANNELS, longListTypeToken);
        nodeTypeMap.put(ConfigKeys.DISCORD_STAFF_CHANNELS, longListTypeToken);

        nodeTypeMap.put(ConfigKeys.DISCORD_PLAYER_LIST_ENABLED, TypeToken.of(Boolean.class));
        nodeTypeMap.put(ConfigKeys.DISCORD_PLAYER_LIST_FORMAT, TypeToken.of(String.class));
        nodeTypeMap.put(ConfigKeys.DISCORD_PLAYER_LIST_SEPARATOR, TypeToken.of(String.class));
        nodeTypeMap.put(ConfigKeys.DISCORD_PLAYER_LIST_COMMAND_REMOVE_DELAY, TypeToken.of(Integer.class));
        nodeTypeMap.put(ConfigKeys.DISCORD_PLAYER_LIST_RESPONSE_REMOVE_DELAY, TypeToken.of(Integer.class));

        nodeTypeMap.put(ConfigKeys.VELOCITY_JOIN_FORMAT, TypeToken.of(String.class));
        nodeTypeMap.put(ConfigKeys.VELOCITY_LEAVE_FORMAT, TypeToken.of(String.class));
    }

    @Override
    protected void initVerificationMaps() {
        Map<Predicate<String>, Function<String, String>> temp = new HashMap<>();
        temp.put(s -> s.equals("replace"), string -> {
            System.err.println("[MSDBConfig] You need to set a bot token!");
            return null;
        });
        stringVerificationMap.put(ConfigKeys.DISCORD_TOKEN, temp);
    }

    @Override
    protected void initDefaultMaps() {
        defaultStringMap.put(ConfigKeys.DISCORD_TOKEN, "replace");
        defaultListMap.put(ConfigKeys.DISCORD_IN_CHANNELS, new ArrayList<Long>());
        defaultListMap.put(ConfigKeys.DISCORD_OUT_CHANNELS, new ArrayList<Long>());
        defaultListMap.put(ConfigKeys.DISCORD_STAFF_CHANNELS, new ArrayList<Long>());
        defaultBooleanMap.put(ConfigKeys.DISCORD_PLAYER_LIST_ENABLED, true);
        defaultStringMap.put(ConfigKeys.DISCORD_PLAYER_LIST_FORMAT, "**{count} players online:** ```\n{players}\n```");
        defaultStringMap.put(ConfigKeys.DISCORD_PLAYER_LIST_SEPARATOR, ", ");
        defaultIntegerMap.put(ConfigKeys.DISCORD_PLAYER_LIST_COMMAND_REMOVE_DELAY, 0);
        defaultIntegerMap.put(ConfigKeys.DISCORD_PLAYER_LIST_RESPONSE_REMOVE_DELAY, 10);
        defaultStringMap.put(ConfigKeys.VELOCITY_JOIN_FORMAT, "**{player} joined the game");
        defaultStringMap.put(ConfigKeys.VELOCITY_LEAVE_FORMAT, "**{player} left the game**");
    }

    @Override
    protected void initNodeNameMap() {
        nodeNameMap.put(ConfigKeys.DISCORD_TOKEN, "token");
        nodeNameMap.put(ConfigKeys.DISCORD_IN_CHANNELS, "channelsIn");
        nodeNameMap.put(ConfigKeys.DISCORD_OUT_CHANNELS, "channelsOut");
        nodeNameMap.put(ConfigKeys.DISCORD_STAFF_CHANNELS, "channelsStaff");
        nodeNameMap.put(ConfigKeys.DISCORD_PLAYER_LIST_ENABLED, "playerList.enabled");
        nodeNameMap.put(ConfigKeys.DISCORD_PLAYER_LIST_FORMAT, "playerList.format");
        nodeNameMap.put(ConfigKeys.DISCORD_PLAYER_LIST_SEPARATOR, "playerList.separator");
        nodeNameMap.put(ConfigKeys.DISCORD_PLAYER_LIST_COMMAND_REMOVE_DELAY, "playerList.commandRemoveDelay");
        nodeNameMap.put(ConfigKeys.DISCORD_PLAYER_LIST_RESPONSE_REMOVE_DELAY, "playerList.responseRemoveDelay");
        nodeNameMap.put(ConfigKeys.VELOCITY_JOIN_FORMAT, "velocity.joinFormat");
        nodeNameMap.put(ConfigKeys.VELOCITY_LEAVE_FORMAT, "velocity.leaveFormat");
    }

    @Override
    protected void initNodeDescriptionMap() {

        nodeDescriptionMap.put(ConfigKeys.DISCORD_TOKEN, "\nDiscord bot token");
        nodeDescriptionMap.put(ConfigKeys.DISCORD_IN_CHANNELS, "\nDiscord in channels");
        nodeDescriptionMap.put(ConfigKeys.DISCORD_OUT_CHANNELS, "\nDiscord out channels");
        nodeDescriptionMap.put(ConfigKeys.DISCORD_STAFF_CHANNELS, "\nDiscord staff channels");
        nodeDescriptionMap.put(ConfigKeys.DISCORD_PLAYER_LIST_ENABLED, "\nWhether the player list should be enabled");
        nodeDescriptionMap.put(ConfigKeys.DISCORD_PLAYER_LIST_FORMAT, "\nFormat for the player list");
        nodeDescriptionMap.put(ConfigKeys.DISCORD_PLAYER_LIST_SEPARATOR, "\nSeparator to use between players");
        nodeDescriptionMap.put(ConfigKeys.DISCORD_PLAYER_LIST_COMMAND_REMOVE_DELAY, "\nAfter how much time should the command be removed from the discord channel");
        nodeDescriptionMap.put(ConfigKeys.DISCORD_PLAYER_LIST_RESPONSE_REMOVE_DELAY, "\nAfter how much time should the command response be removed from the discord channel");
        nodeDescriptionMap.put(ConfigKeys.VELOCITY_JOIN_FORMAT, "\nJoin message format for players");
        nodeDescriptionMap.put(ConfigKeys.VELOCITY_LEAVE_FORMAT, "\nLeave message format for players");
    }
}

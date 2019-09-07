package essentials.discordbridge.velocity;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.ConnectionHandshakeEvent;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import essentials.discordbridge.Bridge;
import essentials.modules.Config.ConfigKeys;
import essentials.modules.Config.PlayerConfig;
import rocks.milspecsg.msrepository.api.config.ConfigurationService;

import java.util.UUID;

public class VelocityListener {

    @Inject
    Bridge bridge;

    @Inject
    @Named("discord")
    ConfigurationService configurationService;

    @Subscribe
    public void onPlayerJoin(PostLoginEvent event)
    {
        String message = configurationService.getConfigString(ConfigKeys.VELOCITY_JOIN_FORMAT)
                .replaceAll("\\{player}", event.getPlayer().getUsername());
        UUID playerUUID = event.getPlayer().getUniqueId();
        String name = event.getPlayer().getGameProfile().getName();
        PlayerConfig.getPlayerFromFile(playerUUID, name);

        bridge.getOutChannels(bridge.getDiscordApi()).forEach(chan -> chan.sendMessage(message));
}
    @Subscribe
    public void onPlayerQuit(DisconnectEvent event)
    {
        String message = configurationService.getConfigString(ConfigKeys.VELOCITY_LEAVE_FORMAT)
                .replace("{player}", event.getPlayer().getUsername());
        String playerid = event.getPlayer().getUniqueId().toString();
        PlayerConfig.setLastSeen(playerid);

        bridge.getOutChannels(bridge.getDiscordApi()).forEach(chan -> chan.sendMessage(message));
    }


}

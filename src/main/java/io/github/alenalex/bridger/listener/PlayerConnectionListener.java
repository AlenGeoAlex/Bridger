package io.github.alenalex.bridger.listener;

import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.models.player.UserData;
import io.github.alenalex.bridger.variables.LangConfigurationPaths;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public final class PlayerConnectionListener implements Listener {

    private final Bridger plugin;

    public PlayerConnectionListener(Bridger plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerJoin(PlayerJoinEvent event){
        final Player player = event.getPlayer();
        final UUID playerUUID = player.getUniqueId();

        event.setJoinMessage(plugin.configurationHandler().getConfigurationFile().getServerJoinMessage());

        plugin.dataProvider().getDatabaseProvider().loadOrRegisterUser(playerUUID).thenAccept(user -> {
           if(user == null){
               plugin.getLogger().warning("Failed to load user " + playerUUID.toString());
               player.kickPlayer(plugin.localeManager().onDefault().asLegacyColorizedString(LangConfigurationPaths.KICK_MESSAGE_FAILED_TO_LOAD_DATA.getPath()));
               return;
           }

           plugin.gameHandler().userManager().registerOverride(playerUUID, user);
        });
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerDisconnect(PlayerQuitEvent event){

        final UUID playerUUID = event.getPlayer().getUniqueId();
        final UserData userData = plugin.gameHandler().userManager().of(playerUUID);

        event.setQuitMessage(plugin.configurationHandler().getConfigurationFile().getServerLeaveMessage());

        if(userData == null){
            plugin.getLogger().warning("Failed to save user " + event.getPlayer().getName()+". The data returned null from registry.");
            return;
        }

        plugin.gameHandler().onPlayerQuit(event.getPlayer());
        plugin.setupSessionManager().onPlayerQuit(playerUUID);

        plugin.dataProvider().getDatabaseProvider().saveUserAsync(userData);

        plugin.gameHandler().userManager().remove(playerUUID);
        plugin.gameHandler().userManager().removeBuildPermsToPlayer(playerUUID);
    }
}

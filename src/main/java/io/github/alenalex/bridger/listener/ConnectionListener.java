package io.github.alenalex.bridger.listener;

import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.models.player.UserData;
import io.github.alenalex.bridger.variables.LangConfigurationPaths;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class ConnectionListener implements Listener {

    private final Bridger plugin;

    public ConnectionListener(Bridger plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        final Player player = event.getPlayer();
        final UUID playerUUID = player.getUniqueId();

        plugin.dataProvider().getDatabaseProvider().loadOrRegisterUser(playerUUID).thenAccept(user -> {
           if(user == null){
               plugin.getLogger().warning("Failed to load user " + playerUUID.toString());
               player.kickPlayer(plugin.localManager().onDefault().asLegacyColorizedString(LangConfigurationPaths.KICK_MESSAGE_FAILED_TO_LOAD_DATA.getPath()));
               return;
           }

           plugin.gameHandler().userManager().registerOverride(playerUUID, user);
        });
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent event){

        final UUID playerUUID = event.getPlayer().getUniqueId();
        final UserData userData = plugin.gameHandler().userManager().of(playerUUID);

        if(userData == null){
            plugin.getLogger().warning("Failed to save user " + event.getPlayer().getName()+". The data returned null from registry.");
            return;
        }

        plugin.gameHandler().playerQuitServer(event.getPlayer());
        plugin.setupSessionManager().onPlayerQuit(playerUUID);

        plugin.dataProvider().getDatabaseProvider().saveUserAsync(userData);

        plugin.gameHandler().userManager().remove(playerUUID);
        plugin.gameHandler().userManager().removeBuildPermsToPlayer(playerUUID);
    }
}

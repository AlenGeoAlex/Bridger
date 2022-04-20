package io.github.alenalex.bridger.listener;

import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.manager.UserManager;
import io.github.alenalex.bridger.models.Island;
import io.github.alenalex.bridger.models.player.UserData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerDeathListener implements Listener {

    private final Bridger plugin;

    public PlayerDeathListener(Bridger plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        event.getDrops().clear();
        event.setDroppedExp(0);
        event.setDeathMessage(null);

        final Player player = event.getEntity();
        final UserData userData = plugin.gameHandler().userManager().of(player.getUniqueId());

        if(userData == null)
            return;

        switch (userData.userMatchCache().getStatus()) {
            case LOBBY:{
                UserManager.handleLobbyTransport(player);
                break;
            }
            case SPECTATING:{
                plugin.gameHandler().islandManager().stopSpectating(player, userData);
                break;
            }
            case PLAYING:{
                player.spigot().respawn();
                plugin.gameHandler().playerFailedGame(player);
                break;
            }
            case IDLE:{
                player.spigot().respawn();
                Island island = plugin.gameHandler().getIslandOfPlayer(player).orElse(null);
                if(island == null)
                    return;
                plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                    @Override
                    public void run() {
                        island.teleportToSpawn(player);
                    }
                }, 10L);
                break;
            }
            default:

        }
    }

}

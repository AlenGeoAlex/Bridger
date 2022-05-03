package io.github.alenalex.bridger.listener;

import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.manager.UserManagerImpl;
import io.github.alenalex.bridger.models.BridgerIsland;
import io.github.alenalex.bridger.models.player.BridgerUserData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public final class PlayerDeathListener implements Listener {

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
        final BridgerUserData bridgerUserData = plugin.gameHandler().userManager().of(player.getUniqueId());

        if(bridgerUserData == null)
            return;

        switch (bridgerUserData.userMatchCache().getStatus()) {
            case LOBBY:{
                UserManagerImpl.handleLobbyTransport(player);
                break;
            }
            case SPECTATING:{
                plugin.gameHandler().islandManager().stopSpectating(player, bridgerUserData);
                break;
            }
            case PLAYING:{
                player.spigot().respawn();
                plugin.gameHandler().playerFailedGame(player);
                break;
            }
            case IDLE:{
                player.spigot().respawn();
                BridgerIsland bridgerIsland = plugin.gameHandler().getIslandOfPlayer(player).orElse(null);
                if(bridgerIsland == null)
                    return;
                plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                    @Override
                    public void run() {
                        bridgerIsland.teleportToSpawn(player);
                    }
                }, 10L);
                break;
            }
            default:

        }
    }

}

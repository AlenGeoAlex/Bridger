package io.github.alenalex.bridger.listener;

import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.variables.Permissions;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class PlayerInteractListener implements Listener {

    private final Bridger plugin;

    public PlayerInteractListener(Bridger plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if(event.isCancelled()) return;

        final Player player = event.getPlayer() ;

        if(player.hasPermission(Permissions.Admin.ADMIN_DROP_ITEM)
                || plugin.gameHandler().userManager().isPlayerAllowedToBuild(player))
            return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerItemPickupEvent(PlayerPickupItemEvent event){
        if(event.isCancelled())
            return;

        final Player player = event.getPlayer();

        if(player.hasPermission(Permissions.Admin.ADMIN_PICKUP_ITEM)
                || plugin.gameHandler().userManager().isPlayerAllowedToBuild(player))
            return;

        event.setCancelled(true);
    }
}

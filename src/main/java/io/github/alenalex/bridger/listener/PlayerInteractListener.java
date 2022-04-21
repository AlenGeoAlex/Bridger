package io.github.alenalex.bridger.listener;

import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.models.player.UserData;
import io.github.alenalex.bridger.variables.Permissions;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class PlayerInteractListener implements Listener {

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

    @EventHandler
    public void onPlayerHotBarEvent(@NotNull PlayerInteractEvent event){
        if(event.isCancelled())
            return;

        if(event.getItem() != null && !event.getItem().hasItemMeta())
            return;

        final ItemStack clickedItem = event.getItem();

        if(!clickedItem.getItemMeta().hasDisplayName())
            return;

        plugin.getLogger().info(clickedItem.getItemMeta().getDisplayName());

        final Player player = event.getPlayer();
        final UserData userData = plugin.gameHandler().userManager().of(player.getUniqueId());

        if(userData == null)
            return;

        switch (userData.userMatchCache().getStatus()){
            case IDLE:
            case PLAYING:
            {

                break;
            }
            case LOBBY:
            {



                break;
            }
            case SPECTATING:
            {

                break;
            }
            default:

        }
    }
}

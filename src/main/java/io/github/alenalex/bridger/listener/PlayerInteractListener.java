package io.github.alenalex.bridger.listener;

import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.ui.config.HotBarConfig;
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
        System.out.println("PlayerInteractListener");
        System.out.println(1);

        if(event.getItem() == null || !event.getItem().hasItemMeta())
            return;

        System.out.println(2);

        final ItemStack clickedItem = event.getItem();

        if(!clickedItem.getItemMeta().hasDisplayName())
            return;

        System.out.println(3);
        plugin.getLogger().info(clickedItem.getItemMeta().getDisplayName());

        final Player player = event.getPlayer();
        final UserData userData = plugin.gameHandler().userManager().of(player.getUniqueId());

        if(userData == null)
            return;

        switch (userData.userMatchCache().getStatus()){
            case IDLE:
            case PLAYING:
            {
                if(plugin.configurationHandler().getConfigurationFile().getMatchLeaveItem().isInteractionSimilar(event.getItem())) {
                    event.setCancelled(true);
                    player.performCommand("island leave");
                }
                break;
            }
            case LOBBY:
            {
                if(plugin.configurationHandler().getConfigurationFile().getLobbyJoin() != null){
                    if(plugin.configurationHandler().getConfigurationFile().getLobbyJoin().isInteractionSimilar(event.getItem())){
                        event.setCancelled(true);
                        player.performCommand("island");
                        return;
                    }
                }

                if(plugin.configurationHandler().getConfigurationFile().getLobbySettings() != null){
                    if(plugin.configurationHandler().getConfigurationFile().getLobbySettings().isInteractionSimilar(event.getItem())){
                        event.setCancelled(true);
                        plugin.uiHandler().getPlayerSettings().openFor(player);
                        return;
                    }
                }

                if(plugin.configurationHandler().getConfigurationFile().getLobbySelector() != null){
                    if(plugin.configurationHandler().getConfigurationFile().getLobbySelector().isInteractionSimilar(event.getItem())){
                        event.setCancelled(true);
                        plugin.uiHandler().getIslandSelector().openFor(player);
                        return;
                    }
                }

                if(plugin.configurationHandler().getConfigurationFile().getLobbyShop() != null){
                    if(plugin.configurationHandler().getConfigurationFile().getLobbyShop().isInteractionSimilar(event.getItem())){
                        event.setCancelled(true);
                        plugin.uiHandler().getCosmeticShop().openFor(player);
                        return;
                    }
                }

                for(HotBarConfig config : plugin.configurationHandler().getConfigurationFile().getLobbyOther()){
                    if(config.isInteractionSimilar(event.getItem())){
                        config.performCommands(player);
                        break;
                    }
                }
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

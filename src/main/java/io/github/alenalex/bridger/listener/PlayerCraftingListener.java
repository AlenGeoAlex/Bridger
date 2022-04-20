package io.github.alenalex.bridger.listener;

import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.models.player.UserData;
import io.github.alenalex.bridger.variables.Permissions;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

public class PlayerCraftingListener implements Listener {

    private final Bridger plugin;

    public PlayerCraftingListener(Bridger plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerCraftingEvent(CraftItemEvent event){
        if(event.isCancelled())
            return;

        final Player player = (Player) event.getWhoClicked();
        final UserData userData = plugin.gameHandler().userManager().of(player.getUniqueId());

        if(userData == null)
            return;

        if(player.hasPermission(Permissions.Admin.ADMIN_CRAFTING))
            return;

        event.setCancelled(true);

    }
}

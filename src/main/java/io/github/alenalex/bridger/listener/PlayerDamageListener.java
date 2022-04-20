package io.github.alenalex.bridger.listener;

import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.models.player.UserData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;


public class PlayerDamageListener implements Listener {

    private final Bridger plugin;

    public PlayerDamageListener(Bridger plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDamageEvent(EntityDamageEvent event){
        if(event.isCancelled()) return;

        if(!(event.getEntity() instanceof org.bukkit.entity.Player))
            return;

        final Player player = (Player) event.getEntity();

        final UserData userData = plugin.gameHandler().userManager().of(player.getUniqueId());

        if(userData == null)
            return;

        switch (userData.userMatchCache().getStatus()){

            case IDLE:
            case PLAYING:
            case SPECTATING:
                event.setCancelled(true);
            break;

            default:
        }
    }
}

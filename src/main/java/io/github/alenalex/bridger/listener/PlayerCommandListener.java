package io.github.alenalex.bridger.listener;

import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.models.player.UserData;
import io.github.alenalex.bridger.variables.LangConfigurationPaths;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public final class PlayerCommandListener implements Listener {

    private final Bridger plugin;

    public PlayerCommandListener(Bridger plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerCommandProcessEvent(PlayerCommandPreprocessEvent event){
        if(event.isCancelled())
            return;

        final Player player = event.getPlayer();

        if(!plugin.gameHandler().isPlayerPlaying(player))
            return;

        final String command = event.getMessage();
        if(plugin.configurationHandler().getConfigurationFile().getCommandToBlock().contains(command)){
            event.setCancelled(true);
            final UserData userData = plugin.gameHandler().userManager().of(player.getUniqueId());

            if(userData == null)
                return;

            plugin.messagingUtils().sendTo(player, userData.userSettings().getLanguage().asComponent(LangConfigurationPaths.BLOCKED_COMMAND));
        }
    }
}

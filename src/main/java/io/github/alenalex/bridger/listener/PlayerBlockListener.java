package io.github.alenalex.bridger.listener;

import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.api.events.PlayerBridgingStartedEvent;
import io.github.alenalex.bridger.models.Island;
import io.github.alenalex.bridger.models.player.UserData;
import io.github.alenalex.bridger.models.player.UserMatchCache;
import io.github.alenalex.bridger.variables.LangConfigurationPaths;
import io.github.alenalex.bridger.variables.Permissions;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.Optional;

public final class PlayerBlockListener implements Listener {

    private final Bridger plugin;

    public PlayerBlockListener(Bridger plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        if(event.isCancelled())
            return;

        final Player player = event.getPlayer();
        final UserData userData = plugin.gameHandler().userManager().of(player.getUniqueId());

        if(userData == null)
            return;

        switch (userData.userMatchCache().getStatus()){
            //Start the game
            case IDLE: {
                if(plugin.configurationHandler().getConfigurationFile().getMatchLeaveItem().isInteractionSimilar(event.getItemInHand())){
                    event.setCancelled(true);
                }

                Island island = plugin.gameHandler().getIslandOfPlayer(player).orElse(null);
                if (island == null) return;


                if (island.isIslandResetting()) {
                    event.setCancelled(true);
                    plugin.messagingUtils().sendTo(player, userData.userSettings().getLanguage().asComponent(LangConfigurationPaths.ISLAND_STILL_RESETTING_BLOCKS_PLACED));
                    return;
                }

                final PlayerBridgingStartedEvent playerBridgingStartedEvent = new PlayerBridgingStartedEvent(userData.getPlayer(), userData);
                plugin.getServer().getPluginManager().callEvent(playerBridgingStartedEvent);

                if(event.isCancelled())
                    return;

                plugin.gameHandler().playerFirstBlock(userData);
                userData.userMatchCache().addBlockToCache(event.getBlockPlaced());
                plugin.messagingUtils().sendTo(player, userData.userSettings().getLanguage().asComponent(LangConfigurationPaths.PLAYER_STARTED_BRIDGING));
                break;
            }
                //Deny if player doesn't have appropriate perms
            case LOBBY: {
                if (player.getGameMode() == GameMode.CREATIVE
                        || player.hasPermission(Permissions.Admin.ADMIN_BUILD)
                        || plugin.gameHandler().userManager().isPlayerAllowedToBuild(player)
                )
                    return;

                if (!plugin.configurationHandler().getConfigurationFile().isDoAllowPlacingBlocksOnLobby())
                    event.setCancelled(true);
                break;
            }
                //Nothing special, just add those blocks to the cache
            case PLAYING: {
                if(plugin.configurationHandler().getConfigurationFile().getMatchLeaveItem().isInteractionSimilar(event.getItemInHand())){
                    event.setCancelled(true);
                }

                if (plugin.configurationHandler().getConfigurationFile().getPlacementBlockedMaterial().contains(event.getBlockAgainst().getType())) {
                    event.setCancelled(true);
                    plugin.messagingUtils().sendTo(player, userData.userSettings().getLanguage().asComponent(LangConfigurationPaths.CANNOT_PLACE_BLOCK_HERE));
                    return;
                }

                final Island island = plugin.gameHandler().getIslandOfPlayer(player).orElse(null);
                if (island == null)
                    return;

                if (event.getBlockPlaced().getLocation().equals(island.getEndLocation())) {
                    event.setCancelled(true);
                    plugin.messagingUtils().sendTo(player, userData.userSettings().getLanguage().asComponent(LangConfigurationPaths.CANNOT_PLACE_BLOCK_HERE));
                    return;
                }

                userData.userMatchCache().addBlockToCache(event.getBlockPlaced());
                break;
            }
            case SPECTATING: {
                if (!player.hasPermission(Permissions.Spectator.BLOCK_PLACE)) {
                    event.setCancelled(true);
                    plugin.messagingUtils().sendTo(player, userData.userSettings().getLanguage().asComponent(LangConfigurationPaths.CANNOT_PLACE_BLOCKS_WHILE_SPECTATING));
                }
                break;
            }
            default:
                return;
        };
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event){
        if(event.isCancelled())
            return;

        final Player player = event.getPlayer();
        final UserData userData = plugin.gameHandler().userManager().of(player.getUniqueId());

        if(userData == null)
            return;

        if(player.getGameMode() == GameMode.CREATIVE
                || player.hasPermission(Permissions.Admin.ADMIN_BUILD)
                || plugin.gameHandler().userManager().isPlayerAllowedToBuild(player)
        )
            return;

        if(userData.userMatchCache().getStatus() == UserMatchCache.Status.LOBBY){
            if(!plugin.configurationHandler().getConfigurationFile().isDoAllowBreakingBlocksOnLobby())
                event.setCancelled(true);
        }else {
            event.setCancelled(true);
        }
    }
}

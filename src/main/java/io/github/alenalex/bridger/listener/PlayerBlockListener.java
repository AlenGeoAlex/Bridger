package io.github.alenalex.bridger.listener;

import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.models.BridgerIsland;
import io.github.alenalex.bridger.models.player.BridgerUserData;
import io.github.alenalex.bridger.models.player.BridgerUserMatchCache;
import io.github.alenalex.bridger.variables.LangConfigurationPaths;
import io.github.alenalex.bridger.variables.Permissions;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

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
        final BridgerUserData bridgerUserData = plugin.gameHandler().userManager().of(player.getUniqueId());

        if(bridgerUserData == null)
            return;

        switch (bridgerUserData.userMatchCache().getStatus()){
            //Start the game
            case IDLE: {
                BridgerIsland bridgerIsland = plugin.gameHandler().getIslandOfPlayer(player).orElse(null);
                if (bridgerIsland == null) return;

                if (bridgerIsland.isIslandResetting()) {
                    event.setCancelled(true);
                    plugin.messagingUtils().sendTo(player, bridgerUserData.userSettings().getLanguage().asComponent(LangConfigurationPaths.ISLAND_STILL_RESETTING_BLOCKS_PLACED));
                    return;
                }

                plugin.gameHandler().playerFirstBlock(bridgerUserData);
                bridgerUserData.userMatchCache().addBlockToCache(event.getBlockPlaced());
                plugin.messagingUtils().sendTo(player, bridgerUserData.userSettings().getLanguage().asComponent(LangConfigurationPaths.PLAYER_STARTED_BRIDGING));
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
                if (plugin.configurationHandler().getConfigurationFile().getPlacementBlockedMaterial().contains(event.getBlockAgainst().getType())) {
                    event.setCancelled(true);
                    plugin.messagingUtils().sendTo(player, bridgerUserData.userSettings().getLanguage().asComponent(LangConfigurationPaths.CANNOT_PLACE_BLOCK_HERE));
                    return;
                }

                final BridgerIsland bridgerIsland = plugin.gameHandler().getIslandOfPlayer(player).orElse(null);
                if (bridgerIsland == null)
                    return;

                if (event.getBlockPlaced().getLocation().equals(bridgerIsland.getEndLocation())) {
                    event.setCancelled(true);
                    plugin.messagingUtils().sendTo(player, bridgerUserData.userSettings().getLanguage().asComponent(LangConfigurationPaths.CANNOT_PLACE_BLOCK_HERE));
                    return;
                }

                bridgerUserData.userMatchCache().addBlockToCache(event.getBlockPlaced());
                break;
            }
            case SPECTATING: {
                if (!player.hasPermission(Permissions.Spectator.BLOCK_PLACE)) {
                    event.setCancelled(true);
                    plugin.messagingUtils().sendTo(player, bridgerUserData.userSettings().getLanguage().asComponent(LangConfigurationPaths.CANNOT_PLACE_BLOCKS_WHILE_SPECTATING));
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
        final BridgerUserData bridgerUserData = plugin.gameHandler().userManager().of(player.getUniqueId());

        if(bridgerUserData == null)
            return;

        if(bridgerUserData.userMatchCache().getStatus() == BridgerUserMatchCache.Status.LOBBY){
            if(player.getGameMode() == GameMode.CREATIVE
                    || player.hasPermission(Permissions.Admin.ADMIN_BUILD)
                    || plugin.gameHandler().userManager().isPlayerAllowedToBuild(player)
            )
                return;

            if(!plugin.configurationHandler().getConfigurationFile().isDoAllowBreakingBlocksOnLobby())
                event.setCancelled(true);
        }else {
            event.setCancelled(true);
        }
    }
}

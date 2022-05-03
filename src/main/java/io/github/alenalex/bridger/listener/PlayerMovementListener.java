package io.github.alenalex.bridger.listener;

import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.manager.UserManagerImpl;
import io.github.alenalex.bridger.models.BridgerIsland;
import io.github.alenalex.bridger.models.player.BridgerUserData;
import io.github.alenalex.bridger.utils.LocationUtils;
import io.github.alenalex.bridger.utils.adventure.MessageFormatter;
import io.github.alenalex.bridger.variables.LangConfigurationPaths;
import io.github.alenalex.bridger.variables.PluginResponses;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public final class PlayerMovementListener implements Listener {

    private final Bridger plugin;

    public PlayerMovementListener(Bridger plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event){
        if(event.isCancelled())
            return;

        if (event.getTo().getBlockX() == event.getFrom().getBlockX() && event.getTo().getBlockY() == event.getFrom().getBlockY() && event.getTo().getBlockZ() == event.getFrom().getBlockZ()) {
            return;
        }

        final Location to = event.getTo();
        final Location from = event.getFrom();

        final Player player = event.getPlayer();
        final BridgerUserData bridgerUserData = plugin.gameHandler().userManager().of(player.getUniqueId());

        if(bridgerUserData == null)
            return;


        switch (bridgerUserData.userMatchCache().getStatus()){

            case LOBBY: {
                if (to.getBlockY() <= plugin.configurationHandler().getConfigurationFile().getVoidDetectionHeight()) {
                    UserManagerImpl.handleLobbyTransport(player);

                    plugin.messagingUtils().sendTo(player, bridgerUserData.userSettings().getLanguage().asComponent(LangConfigurationPaths.TELEPORTED_VOID_DETECTION));
                    return;
                }
                break;
            }
            case SPECTATING: {
                if (to.getBlockY() <= plugin.configurationHandler().getConfigurationFile().getVoidDetectionHeight()) {
                    BridgerIsland bridgerIsland = plugin.gameHandler().islandManager().of(bridgerUserData.userMatchCache().getSpectatingIsland());
                    if (bridgerIsland == null) {
                        plugin.gameHandler().islandManager().stopSpectating(player, bridgerUserData);
                        plugin.messagingUtils().sendTo(player, MessageFormatter.transform(PluginResponses.Others.UNKNOWN_CAUSE));
                        return;
                    }
                    final Player islandOwner = plugin.gameHandler().getPlayerOfIsland(bridgerIsland.getIslandName()).orElse(null);
                    if (islandOwner == null) {
                        plugin.gameHandler().islandManager().stopSpectating(player, bridgerUserData);
                        plugin.messagingUtils().sendTo(player, MessageFormatter.transform(PluginResponses.Others.UNKNOWN_CAUSE));
                        return;
                    }

                    player.teleport(islandOwner);
                    plugin.messagingUtils().sendTo(player, bridgerUserData.userSettings().getLanguage().asComponent(LangConfigurationPaths.TELEPORTED_VOID_DETECTION));
                    return;
                }
                break;
            }
            case IDLE: {
                BridgerIsland bridgerIsland = plugin.gameHandler().getIslandOfPlayer(player).orElse(null);
                if(bridgerIsland == null)
                    return;

                if (to.getBlockY() <= plugin.configurationHandler().getConfigurationFile().getVoidDetectionHeight()) {
                    bridgerIsland.teleportToSpawn(player);
                    return;
                }

                if (plugin.configurationHandler().getConfigurationFile().isCheatDetectionIdleCompletion()) {
                    if (to.equals(bridgerIsland.getEndLocation())) {
                        player.kickPlayer(bridgerUserData.userSettings().getLanguage().asLegacyColorizedString(LangConfigurationPaths.CHEAT_PROTECTION_REACHED_IN_IDLE.getPath()));
                    }
                }
                break;
            }
            case PLAYING: {
                BridgerIsland bridgerIsland = plugin.gameHandler().getIslandOfPlayer(player).orElse(null);
                if(bridgerIsland == null)
                    return;

                if (to.getBlockY() <= plugin.configurationHandler().getConfigurationFile().getVoidDetectionHeight()) {
                    plugin.gameHandler().playerFailedGame(player);
                    return;
                }



                if(LocationUtils.isSameLocation(to, bridgerIsland.getEndLocation())) {
                    if(!(bridgerUserData.userMatchCache().getBlocksPlaced() >= bridgerIsland.getMinBlocksRequired())){
                        if(plugin.configurationHandler().getConfigurationFile().isCheatDetectionMinBlocks()) {
                            player.kickPlayer(MessageFormatter.colorizeLegacy("&c&lCheat detection: &cYou didn't satisfy the islands condition!"));
                        }else {
                            plugin.gameHandler().playerFailedGame(player);
                            plugin.messagingUtils().sendTo(player, bridgerUserData.userSettings().getLanguage().asComponent(LangConfigurationPaths.CHEAT_PROTECTION_MIN_BLOCK));
                        }
                       return;
                    }

                    final long timeTook =  bridgerUserData.userMatchCache().getStartTime();

                    if(!(timeTook > bridgerIsland.getMinTimeRequired())) {
                        if(plugin.configurationHandler().getConfigurationFile().isCheatDetectionMinTime()) {
                            player.kickPlayer(MessageFormatter.colorizeLegacy("&c&lCheat detection: &cYou didn't satisfy the islands condition!"));
                        }else {
                            plugin.gameHandler().playerFailedGame(player);
                            plugin.messagingUtils().sendTo(player, bridgerUserData.userSettings().getLanguage().asComponent(LangConfigurationPaths.CHEAT_PROTECTION_MIN_TIME));
                        }
                        return;
                    }

                    plugin.gameHandler().playerCompleteGame(player, timeTook);
                }
                break;
            }
            default:
        }
    }
}

package io.github.alenalex.bridger.listener;

import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.manager.UserManager;
import io.github.alenalex.bridger.models.Island;
import io.github.alenalex.bridger.models.player.UserData;
import io.github.alenalex.bridger.utils.adventure.MessageFormatter;
import io.github.alenalex.bridger.variables.LangConfigurationPaths;
import io.github.alenalex.bridger.variables.PluginResponses;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMovementListener implements Listener {

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
        final UserData userData = plugin.gameHandler().userManager().of(player.getUniqueId());

        if(userData == null)
            return;

        switch (userData.userMatchCache().getStatus()){

            case LOBBY: {
                if (to.getBlockY() <= plugin.configurationHandler().getConfigurationFile().getVoidDetectionHeight()) {
                    UserManager.handleLobbyTransport(player);

                    plugin.messagingUtils().sendTo(player, userData.userSettings().getLanguage().asComponent(LangConfigurationPaths.TELEPORTED_VOID_DETECTION));
                    return;
                }
                break;
            }
            case SPECTATING: {
                if (to.getBlockY() <= plugin.configurationHandler().getConfigurationFile().getVoidDetectionHeight()) {
                    Island island = plugin.gameHandler().islandManager().of(userData.userMatchCache().getSpectatingIsland());
                    if (island == null) {
                        plugin.gameHandler().islandManager().stopSpectating(player, userData);
                        plugin.messagingUtils().sendTo(player, MessageFormatter.transform(PluginResponses.Others.UNKNOWN_CAUSE));
                        return;
                    }
                    final Player islandOwner = plugin.gameHandler().getPlayerOfIsland(island.getIslandName()).orElse(null);
                    if (islandOwner == null) {
                        plugin.gameHandler().islandManager().stopSpectating(player, userData);
                        plugin.messagingUtils().sendTo(player, MessageFormatter.transform(PluginResponses.Others.UNKNOWN_CAUSE));
                        return;
                    }

                    player.teleport(islandOwner);
                    plugin.messagingUtils().sendTo(player, userData.userSettings().getLanguage().asComponent(LangConfigurationPaths.TELEPORTED_VOID_DETECTION));
                    return;
                }
                break;
            }
            case IDLE: {
                Island island = plugin.gameHandler().islandManager().of(userData.userMatchCache().getSpectatingIsland());

                if (to.getBlockY() <= plugin.configurationHandler().getConfigurationFile().getVoidDetectionHeight()) {
                    island.teleportToSpawn(player);
                    return;
                }

                if (plugin.configurationHandler().getConfigurationFile().isCheatDetectionIdleCompletion()) {
                    if (to.equals(island.getEndLocation())) {
                        player.kickPlayer(userData.userSettings().getLanguage().asLegacyColorizedString(LangConfigurationPaths.CHEAT_PROTECTION_REACHED_IN_IDLE.getPath()));
                    }
                }
                break;
            }
            case PLAYING: {
                Island island = plugin.gameHandler().islandManager().of(userData.userMatchCache().getSpectatingIsland());

                if (to.getBlockY() <= plugin.configurationHandler().getConfigurationFile().getVoidDetectionHeight()) {
                    plugin.gameHandler().playerFailedGame(player);
                    return;
                }

                if(to.equals(island.getEndLocation())) {
                    if(!(userData.userMatchCache().getBlocksPlaced() >= island.getMinBlocksRequired())){
                        if(plugin.configurationHandler().getConfigurationFile().isCheatDetectionMinBlocks()) {
                            player.kickPlayer(MessageFormatter.colorizeLegacy("&c&lCheat detection: &cYou didn't satisfy the islands condition!"));
                        }else {
                            plugin.gameHandler().playerFailedGame(player);
                            plugin.messagingUtils().sendTo(player, userData.userSettings().getLanguage().asComponent(LangConfigurationPaths.CHEAT_PROTECTION_MIN_BLOCK));
                        }
                       return;
                    }

                    final long timeTook =  userData.userMatchCache().getStartTime();

                    if(!(timeTook > island.getMinTimeRequired())) {
                        if(plugin.configurationHandler().getConfigurationFile().isCheatDetectionMinTime()) {
                            player.kickPlayer(MessageFormatter.colorizeLegacy("&c&lCheat detection: &cYou didn't satisfy the islands condition!"));
                        }else {
                            plugin.gameHandler().playerFailedGame(player);
                            plugin.messagingUtils().sendTo(player, userData.userSettings().getLanguage().asComponent(LangConfigurationPaths.CHEAT_PROTECTION_MIN_TIME));
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

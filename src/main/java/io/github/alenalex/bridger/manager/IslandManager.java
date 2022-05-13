package io.github.alenalex.bridger.manager;

import de.leonhard.storage.internal.FlatFile;
import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.abstracts.AbstractRegistry;
import io.github.alenalex.bridger.exceptions.IllegalRegistryOperation;
import io.github.alenalex.bridger.models.Island;
import io.github.alenalex.bridger.models.player.UserData;
import io.github.alenalex.bridger.models.player.UserMatchCache;
import io.github.alenalex.bridger.utils.FlatFileUtils;
import io.github.alenalex.bridger.utils.adventure.internal.MessagePlaceholder;
import io.github.alenalex.bridger.variables.LangConfigurationPaths;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class IslandManager extends AbstractRegistry<String, Island> {

    public IslandManager(Bridger plugin) {
        super(plugin);
    }

    public void loadAllIslands(){
        final FlatFile islandConfig = plugin.configurationHandler().getIslandConfiguration().file();
        if(islandConfig == null)
            return;

        for(String islandName : islandConfig.singleLayerKeySet()) {
            final boolean enabled = islandConfig.getBoolean(islandName + ".enabled");
            final Location pos1 = FlatFileUtils.deserializeLocation(islandConfig.getSection(islandName + ".pos1")).orElse(null);
            final Location pos2 = FlatFileUtils.deserializeLocation(islandConfig.getSection(islandName + ".pos2")).orElse(null);
            final Location spawn = FlatFileUtils.deserializeLocation(islandConfig.getSection(islandName + ".spawn")).orElse(null);
            final Location end = FlatFileUtils.deserializeLocation(islandConfig.getSection(islandName + ".end")).orElse(null);

            if (pos1 != null && pos2 != null && spawn != null && end != null) {
                final String perm = islandConfig.contains(islandName + ".perm") ? islandConfig.getString(islandName + ".perm") : null;
                final int minBlocks = islandConfig.getInt(islandName + ".min-req.blocks");
                final int minSec = islandConfig.getInt(islandName + ".min-req.sec");
                final double joinCost = islandConfig.getDouble(islandName + ".join-cost");
                final double reward = islandConfig.getDouble(islandName + ".reward-coins");

                final Island island =
                        new Island(
                                islandName,
                                perm,
                                spawn,
                                end,
                                pos1,
                                pos2,
                                minSec,
                                minBlocks,
                                joinCost,
                                reward
                        );

                island.setEnabled(enabled);

                plugin.getLogger().info("Loaded island with name " + islandName + "!");
                registerIsland(island);
            } else {
                plugin.getLogger().warning("Failed to load island with the name "+islandName+"! Seems like locations are not provided valid!");
            }
        }
    }

    public List<Island> getAllFreeIslands(){
        return getValueStream().collect(Collectors.toList());
    }

    public List<Island> getAllFreeIslands(@NotNull Player player){
        return getValueStream()
                .filter(island -> island.canPlayerJoinTheIsland(player))
                .collect(Collectors.toList());
    }

    public Optional<Island> getAnyFreeIsland(@NotNull Player player){
        return getValueStream()
                .filter(island -> island.canPlayerJoinTheIsland(player))
                .findAny();
    }

    public Optional<Island> getFreeIslandByName(@NotNull Player player, @NotNull String islandName){
        if(!isKeyRegistered(islandName))
            return Optional.empty();

        final Island island = of(islandName);
        if(island.canPlayerJoinTheIsland(player))
            return Optional.of(island);
        else return Optional.empty();
    }

    public List<Island> getAllOccupiedIsland(){
        return getValueStream().filter(Island::isIslandOccupied).collect(Collectors.toList());
    }

    public List<Island> getAllEnabledIsland(){
        return getValueStream().filter(Island::isEnabled).collect(Collectors.toList());
    }

    public List<Island> getAllResettingIslands(){
        return getValueStream().filter(Island::isIslandResetting).collect(Collectors.toList());
    }

    public void disableIsland(@NotNull String islandName){
        if(isKeyRegistered(islandName))
            return;

        final Island island = of(islandName);
        if(island.isIslandOccupied()){
            //TODO
            Player player = plugin.gameHandler().getPlayerOfIsland(islandName).orElse(null);
            if(player == null)
                return;

            UserData userData = plugin.gameHandler().userManager().of(player.getUniqueId());
            if(userData == null)
                return;

            plugin.gameHandler().kickPlayerFromIsland(player);
            userData.userSettings().getLanguage().asComponent(LangConfigurationPaths.KICK_ISLAND_DISABLED);


        }else {
            island.setEnabled(false);
        }
        plugin.hologramHandler().removeHologramOfIsland(island);
    }

    public void removeSpectators(@NotNull Island island){
        if(island.getSpectators().isEmpty())
            return;

        for(UUID spectatingPlayer : island.getSpectators()){
            final Player player = Bukkit.getPlayer(spectatingPlayer);
            if(player == null)
                return;

            final UserData data = plugin.gameHandler().userManager().of(spectatingPlayer);
            if(data == null)
                continue;

            stopSpectating(player, data);
        }
        island.getSpectators().clear();
    }

    public void stopSpectating(@NotNull Player player, UserData userData){
        for(Player serverPlayer : Bukkit.getOnlinePlayers()){
            if(!player.isOnline())
                break;

            if(!serverPlayer.isOnline())
                continue;

            serverPlayer.showPlayer(player);
        }

        UserManager.handleLobbyTransport(player);
    }

    public void startSpectating(@NotNull Player player, Player target){
        final UserData userData = plugin.gameHandler().userManager().of(player.getUniqueId());
        if(userData == null)
            return;

        if(plugin.gameHandler().isPlayerPlaying(target)){
            plugin.messagingUtils().sendTo(player, userData.userSettings().getLanguage().asComponent(LangConfigurationPaths.CANNOT_SPECTATE_NON_PLAYERS));
            return;
        }

        if(!(userData.userMatchCache().getStatus() == UserMatchCache.Status.LOBBY)){
            plugin.messagingUtils().sendTo(player, userData.userSettings().getLanguage().asComponent(LangConfigurationPaths.CANNOT_SPECTATE_WHILE_IN_GAME));
            return;
        }

        final Island island = plugin.gameHandler().getIslandOfPlayer(player).orElse(null);
        if(island == null){
            plugin.messagingUtils().sendTo(player, userData.userSettings().getLanguage().asComponent(LangConfigurationPaths.NO_ISLAND_FOUND));
            return;
        }

        island.addSpectator(player.getUniqueId());
        userData.userMatchCache().setSpectatingIsland(island.getIslandName());


        for(Player serverPlayer : Bukkit.getOnlinePlayers()){
            if(!player.isOnline())
                break;

            if(!serverPlayer.isOnline())
                continue;

            serverPlayer.hidePlayer(player);
        }

        player.teleport(target);
        plugin.messagingUtils().sendTo(player,
                userData.userSettings().getLanguage().asComponent(LangConfigurationPaths.SPECTATING_ON,
                MessagePlaceholder.of("%spec_player%", target.getName())
                )
        );
    }

    public void enableIsland(@NotNull String islandName){
        if(isKeyRegistered(islandName))
            return;

        final Island island = of(islandName);
        island.setEnabled(true);
    }

    public void registerIsland(@NotNull Island island){
        register(island.getIslandName(), island);
        island.setEnabled(true);
    }

    public void reloadIsland(@NotNull String islandName){
        if(!isKeyRegistered(islandName))
            return;

        disableIsland(islandName);
        //TODO
    }




}

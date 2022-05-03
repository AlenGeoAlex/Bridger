package io.github.alenalex.bridger.manager;

import de.leonhard.storage.internal.FlatFile;
import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.abstracts.AbstractRegistry;
import io.github.alenalex.bridger.api.models.Island;
import io.github.alenalex.bridger.api.models.player.UserData;
import io.github.alenalex.bridger.models.BridgerIsland;
import io.github.alenalex.bridger.models.player.BridgerUserData;
import io.github.alenalex.bridger.models.player.BridgerUserMatchCache;
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

public class IslandManagerImpl extends AbstractRegistry<String, BridgerIsland> implements io.github.alenalex.bridger.api.manager.IslandManager {

    public IslandManagerImpl(Bridger plugin) {
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

                final BridgerIsland bridgerIsland =
                        new BridgerIsland(
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

                bridgerIsland.setEnabled(enabled);

                plugin.getLogger().info("Loaded island with name " + islandName + "!");
                registerIsland(bridgerIsland);
            } else {
                plugin.getLogger().warning("Failed to load island with the name "+islandName+"! Seems like locations are not provided valid!");
            }
        }
    }

    @Override
    public List<Island> getAllFreeIslands(){
        return getValueStream().collect(Collectors.toList());
    }

    @Override
    public List<Island> getAllFreeIslands(@NotNull Player player){
        return getValueStream()
                .filter(island -> island.canPlayerJoinTheIsland(player))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Island> getAnyFreeIsland(@NotNull Player player){
        return getValueStream()
                .filter(island -> island.canPlayerJoinTheIsland(player))
                .map(island -> (Island) island)
                .findAny();
    }

    public Optional<Island> getFreeIslandByName(@NotNull Player player, @NotNull String islandName){
        if(!isKeyRegistered(islandName))
            return Optional.empty();

        final BridgerIsland bridgerIsland = of(islandName);
        if(bridgerIsland.canPlayerJoinTheIsland(player))
            return Optional.of(bridgerIsland);
        else return Optional.empty();
    }

    @Override
    public List<Island> getAllOccupiedIsland(){
        return getValueStream().filter(BridgerIsland::isIslandOccupied).collect(Collectors.toList());
    }

    @Override
    public List<Island> getAllEnabledIsland(){
        return getValueStream().filter(BridgerIsland::isEnabled).collect(Collectors.toList());
    }

    @Override
    public List<Island> getAllResettingIslands(){
        return getValueStream().filter(BridgerIsland::isIslandResetting).collect(Collectors.toList());
    }

    @Override
    public void disableIsland(@NotNull String islandName){
        if(isKeyRegistered(islandName))
            return;

        final BridgerIsland bridgerIsland = of(islandName);
        if(bridgerIsland.isIslandOccupied()){
            //TODO
            Player player = plugin.gameHandler().getPlayerOfIsland(islandName).orElse(null);
            if(player == null)
                return;

            BridgerUserData bridgerUserData = plugin.gameHandler().userManager().of(player.getUniqueId());
            if(bridgerUserData == null)
                return;

            plugin.gameHandler().kickPlayerFromIsland(player);
            bridgerUserData.userSettings().getLanguage().asComponent(LangConfigurationPaths.KICK_ISLAND_DISABLED);


        }else {
            bridgerIsland.setEnabled(false);
        }
    }

    @Override
    public void removeSpectators(@NotNull Island island){
        final BridgerIsland bridgerIsland = (BridgerIsland) island;

        if(bridgerIsland.getSpectators().isEmpty())
            return;

        for(UUID spectatingPlayer : bridgerIsland.getSpectators()){
            final Player player = Bukkit.getPlayer(spectatingPlayer);
            if(player == null)
                return;

            final BridgerUserData data = plugin.gameHandler().userManager().of(spectatingPlayer);
            if(data == null)
                continue;

            stopSpectating(player, data);
        }
        bridgerIsland.getSpectators().clear();
    }

    @Override
    public void stopSpectating(@NotNull Player player, UserData uD){
        final BridgerUserData userData = (BridgerUserData) uD;
        for(Player serverPlayer : Bukkit.getOnlinePlayers()){
            if(!player.isOnline())
                break;

            if(!serverPlayer.isOnline())
                continue;

            serverPlayer.showPlayer(player);
        }

        UserManagerImpl.handleLobbyTransport(player);
    }

    @Override
    public void startSpectating(@NotNull Player player, Player target){
        final BridgerUserData bridgerUserData = plugin.gameHandler().userManager().of(player.getUniqueId());
        if(bridgerUserData == null)
            return;

        if(plugin.gameHandler().isPlayerPlaying(target)){
            plugin.messagingUtils().sendTo(player, bridgerUserData.userSettings().getLanguage().asComponent(LangConfigurationPaths.CANNOT_SPECTATE_NON_PLAYERS));
            return;
        }

        if(!(bridgerUserData.userMatchCache().getStatus() == BridgerUserMatchCache.Status.LOBBY)){
            plugin.messagingUtils().sendTo(player, bridgerUserData.userSettings().getLanguage().asComponent(LangConfigurationPaths.CANNOT_SPECTATE_WHILE_IN_GAME));
            return;
        }

        final BridgerIsland bridgerIsland = plugin.gameHandler().getIslandOfPlayer(player).orElse(null);
        if(bridgerIsland == null){
            plugin.messagingUtils().sendTo(player, bridgerUserData.userSettings().getLanguage().asComponent(LangConfigurationPaths.NO_ISLAND_FOUND));
            return;
        }

        bridgerIsland.addSpectator(player.getUniqueId());
        bridgerUserData.userMatchCache().setSpectatingIsland(bridgerIsland.getIslandName());


        for(Player serverPlayer : Bukkit.getOnlinePlayers()){
            if(!player.isOnline())
                break;

            if(!serverPlayer.isOnline())
                continue;

            serverPlayer.hidePlayer(player);
        }

        player.teleport(target);
        plugin.messagingUtils().sendTo(player,
                bridgerUserData.userSettings().getLanguage().asComponent(LangConfigurationPaths.SPECTATING_ON,
                MessagePlaceholder.of("%spec_player%", target.getName())
                )
        );
    }

    @Override
    public void enableIsland(@NotNull String islandName){
        if(isKeyRegistered(islandName))
            return;

        final BridgerIsland bridgerIsland = of(islandName);
        bridgerIsland.setEnabled(true);
    }

    public void registerIsland(@NotNull BridgerIsland bridgerIsland){
        register(bridgerIsland.getIslandName(), bridgerIsland);
        bridgerIsland.setEnabled(true);
    }

    @Override
    public void reloadIsland(@NotNull String islandName){
        if(!isKeyRegistered(islandName))
            return;

        disableIsland(islandName);
        //TODO
    }

    @Override
    public Optional<Island> ofName(@NotNull String name) {
        return Optional.ofNullable(of(name));
    }

}

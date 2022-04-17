package io.github.alenalex.bridger.manager;

import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.abstracts.AbstractRegistry;
import io.github.alenalex.bridger.models.Island;
import io.github.alenalex.bridger.models.player.UserData;
import io.github.alenalex.bridger.variables.LangConfigurationPaths;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.hamcrest.core.Is;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class IslandManager extends AbstractRegistry<String, Island> {

    public IslandManager(Bridger plugin) {
        super(plugin);
    }

    public void loadAllIslands(){

    }

    public List<Island> getAllFreeIslands(){
        return getValueStream().collect(Collectors.toList());
    }


    public Optional<Island> getAnyFreeIsland(@NotNull Player player){
        return getValueStream()
                .filter(island -> {
                    return island.isEnabled() && island.isIslandIdle() && island.hasPermission(player);
                })
                .findAny();
    }

    public Optional<Island> getFreeIslandByName(@NotNull Player player, @NotNull String islandName){
        if(!isKeyRegistered(islandName))
            return Optional.empty();

        final Island island = of(islandName);
        if(island.isEnabled() && island.isIslandIdle() && island.hasPermission(player))
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

            data.userMatchCache().clearSpectatingIsland();
            for(Player serverPlayer : Bukkit.getOnlinePlayers()){
                serverPlayer.showPlayer(player);
            }

            player.teleport(plugin.configurationHandler().getConfigurationFile().getSpawnLocation());
            UserManager.setLobbyItemsOnPlayer(player);
        }
        island.getSpectators().clear();
    }

    public void stopSpectating(@NotNull Player player){

    }

    public void startSpectating(@NotNull Player player, String islandName){

    }

    public void enableIsland(@NotNull String islandName){
        if(isKeyRegistered(islandName))
            return;

        final Island island = of(islandName);
        island.setEnabled(true);
    }

    public void registerIsland(@NotNull Island island){
        if(isKeyRegistered(island.getIslandName()))
            return;

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

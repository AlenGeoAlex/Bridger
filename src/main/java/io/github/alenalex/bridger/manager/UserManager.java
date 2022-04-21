package io.github.alenalex.bridger.manager;

import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.abstracts.AbstractRegistry;
import io.github.alenalex.bridger.gui.config.HotBarConfig;
import io.github.alenalex.bridger.models.player.UserData;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserManager extends AbstractRegistry<UUID, UserData> {

    private final List<UUID> allowBuildOnLobbies;
    public UserManager(Bridger plugin) {
        super(plugin);
        this.allowBuildOnLobbies = new ArrayList<>();
    }

    public static void handleLobbyTransport(@NotNull Player player){
        player.teleport(Bridger.instance().configurationHandler().getConfigurationFile().getSpawnLocation());

        player.setHealth(20);
        player.setGameMode(GameMode.ADVENTURE);
        player.setFlying(false);
        player.setFoodLevel(20);

        if(Bridger.instance().configurationHandler().getConfigurationFile().getLobbyJoin() != null){
            Bridger.instance().configurationHandler().getConfigurationFile().getLobbyJoin().applyOn(player);
        }

        if(Bridger.instance().configurationHandler().getConfigurationFile().getLobbySettings() != null){
            Bridger.instance().configurationHandler().getConfigurationFile().getLobbySettings().applyOn(player);
        }

        if(Bridger.instance().configurationHandler().getConfigurationFile().getLobbySelector() != null){
            Bridger.instance().configurationHandler().getConfigurationFile().getLobbySelector().applyOn(player);
        }

        if(Bridger.instance().configurationHandler().getConfigurationFile().getLobbyShop() != null){
            Bridger.instance().configurationHandler().getConfigurationFile().getLobbyShop().applyOn(player);
        }

        for(HotBarConfig config : Bridger.instance().configurationHandler().getConfigurationFile().getLobbyOther()){
            config.applyOn(player);
        }
    }

    public static void setIslandItemsOnPlayer(@NotNull Player player){

    }

    public static void setBlocksOnPlayer(@NotNull Player player){

    }

    public boolean isPlayerAllowedToBuild(@NotNull Player player){
        return allowBuildOnLobbies.contains(player.getUniqueId());
    }

    public void addBuildPermsToPlayer(@NotNull Player player){
        this.allowBuildOnLobbies.add(player.getUniqueId());
    }

    public void removeBuildPermsToPlayer(@NotNull Player player){
        this.removeBuildPermsToPlayer(player.getUniqueId());
    }

    public void removeBuildPermsToPlayer(@NotNull UUID playerUID){
        this.allowBuildOnLobbies.remove(playerUID);
    }


}

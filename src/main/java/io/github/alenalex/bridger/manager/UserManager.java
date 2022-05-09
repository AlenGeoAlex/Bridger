package io.github.alenalex.bridger.manager;

import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.abstracts.AbstractRegistry;
import io.github.alenalex.bridger.ui.config.HotBarConfig;
import io.github.alenalex.bridger.models.player.UserData;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
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
        Bridger.instance().getServer().getScheduler().runTask(Bridger.instance(), () -> {
            player.teleport(Bridger.instance().configurationHandler().getConfigurationFile().getSpawnLocation());

            player.getInventory().clear();

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
        });

    }

    public static void setIslandItemsOnPlayer(@NotNull UserData userData){
        final Player player = userData.getPlayer();
        if(player == null)
            return;

        player.getInventory().clear();


        player.setHealth(20);
        player.setGameMode(GameMode.SURVIVAL);
        player.setFlying(false);
        player.setFoodLevel(20);

        ItemStack toSet = userData.userSettings().getCurrentBlock();
        for(int invLoop = 0; invLoop <= Bridger.instance().configurationHandler().getConfigurationFile().getBlockCount(); invLoop++){
            player.getInventory().addItem(toSet);
        }

        if(Bridger.instance().configurationHandler().getConfigurationFile().getMatchLeaveItem() != null){
            Bridger.instance().configurationHandler().getConfigurationFile().getMatchLeaveItem().applyOn(player);
        }
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

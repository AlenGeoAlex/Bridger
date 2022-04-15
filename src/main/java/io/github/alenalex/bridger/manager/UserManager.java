package io.github.alenalex.bridger.manager;

import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.abstracts.AbstractRegistry;
import io.github.alenalex.bridger.models.player.UserData;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class UserManager extends AbstractRegistry<UUID, UserData> {

    public UserManager(Bridger plugin) {
        super(plugin);
    }

    public static void setLobbyItemsOnPlayer(@NotNull Player player){

    }

    public static void setIslandItemsOnPlayer(@NotNull Player player){

    }

    public static void setBlocksOnPlayer(@NotNull Player player){

    }


}

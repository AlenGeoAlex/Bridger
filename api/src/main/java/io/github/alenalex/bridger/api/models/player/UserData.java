package io.github.alenalex.bridger.api.models.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public interface UserData {

    @NotNull
    UUID getPlayerUID();

    @NotNull
    Optional<Player> getOptionalPlayer();

    @Nullable
    Player getPlayer();

    /**
     * User cosmetics stores all the data related to user's cosmetic data
     * @return UserCosmetics
     */
    @NotNull
    UserCosmetics userCosmetics();

    @NotNull
    UserMatchCache userMatchCache();

    @NotNull
    UserStats userStats();

    @NotNull
    UserSettings userSettings();
}

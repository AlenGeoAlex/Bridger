package io.github.alenalex.bridger.api.manager;

import io.github.alenalex.bridger.api.models.Island;
import io.github.alenalex.bridger.api.models.player.UserData;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public interface IslandManager {

    List<Island> getAllFreeIslands();

    List<Island> getAllFreeIslands(@NotNull Player player);

    void disableIsland(@NotNull String islandName);

    void removeSpectators(Island island);

    List<Island> getAllOccupiedIsland();

    List<Island> getAllEnabledIsland();

    List<Island> getAllResettingIslands();

    Optional<Island> getAnyFreeIsland(@NotNull Player player);

    void stopSpectating(@NotNull Player player, UserData userData);

    void startSpectating(@NotNull Player player, Player target);

    void enableIsland(@NotNull String islandName);

    void reloadIsland(@NotNull String islandName);

    Optional<Island> ofName(@NotNull String name);


}
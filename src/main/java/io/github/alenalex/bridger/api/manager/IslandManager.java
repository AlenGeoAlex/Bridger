package io.github.alenalex.bridger.api.manager;

import io.github.alenalex.bridger.models.Island;
import io.github.alenalex.bridger.variables.IslandStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;

public interface IslandManager {

    Optional<Island> getIslandByName(@NotNull String islandName);

    boolean isIslandEnabled(@NotNull String islandName);

    boolean isIslandOccupied(@NotNull String islandName);

    Collection<Island> getAllLoadedIslands();

    Collection<Island> getAllOccupiedIslands();

    Collection<Island> getAllEnabledIsland();

    Collection<Island> getAllFreeIslands();

    Optional<IslandStatus> getStatusOf(@NotNull String islandName);

}

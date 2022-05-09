package io.github.alenalex.bridger.api.manager;

import io.github.alenalex.bridger.models.Island;
import io.github.alenalex.bridger.variables.IslandStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;

public interface IslandManager {

    /**
     * Get an island object from an island name
     * @param islandName name of the island
     * @return an Optional of island
     */
    @NotNull Optional<Island> getIslandByName(@NotNull String islandName);

    /**
     * Is the island enabled
     * @param islandName name of the island
     * @return boolean island enabled
     */
    boolean isIslandEnabled(@NotNull String islandName);

    /**
     * Is the island occupied
     * @param islandName name of the island
     * @return boolean island occupied
     */
    boolean isIslandOccupied(@NotNull String islandName);

    /**
     * Get all loaded islands on the cache
     * @return Collection of islands
     */
    @NotNull Collection<Island> getAllLoadedIslands();

    /**
     * Get all occupied islands
     * @return Collection of islands
     */
    @NotNull Collection<Island> getAllOccupiedIslands();

    /**
     * Get all enabled islands
     * @return Collection of islands
     */
    @NotNull Collection<Island> getAllEnabledIsland();

    /**
     * Get all free islands
     * @return Collection of islands
     */
    @NotNull Collection<Island> getAllFreeIslands();

    /**
     * Get status of an island
     * @param islandName name of the island
     * @return Optional IslandStatus
     */
    @NotNull Optional<IslandStatus> getStatusOf(@NotNull String islandName);

}

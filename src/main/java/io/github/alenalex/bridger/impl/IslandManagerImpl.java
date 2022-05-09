package io.github.alenalex.bridger.impl;

import io.github.alenalex.bridger.api.manager.IslandManager;
import io.github.alenalex.bridger.models.Island;
import io.github.alenalex.bridger.variables.IslandStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;

public class IslandManagerImpl implements IslandManager {

    private final io.github.alenalex.bridger.manager.IslandManager islandManager;

    public IslandManagerImpl(io.github.alenalex.bridger.manager.IslandManager islandManager) {
        this.islandManager = islandManager;
    }

    @Override
    public Optional<Island> getIslandByName(@NotNull String islandName) {
        return Optional.ofNullable(islandManager.of(islandName));
    }

    @Override
    public boolean isIslandEnabled(@NotNull String islandName) {
        final Island island = islandManager.of(islandName);
        if(island == null)
            return false;

        return island.isEnabled();
    }

    @Override
    public boolean isIslandOccupied(@NotNull String islandName) {
        final Island island = islandManager.of(islandName);
        if(island == null)
            return false;

        return island.isIslandOccupied();
    }

    @Override
    public Collection<Island> getAllLoadedIslands() {
        return islandManager.getModifiableValueList();
    }

    @Override
    public Collection<Island> getAllOccupiedIslands() {
        return islandManager.getAllOccupiedIsland();
    }

    @Override
    public Collection<Island> getAllEnabledIsland() {
        return islandManager.getAllEnabledIsland();
    }

    @Override
    public Collection<Island> getAllFreeIslands() {
        return islandManager.getAllFreeIslands();
    }

    @Override
    public Optional<IslandStatus> getStatusOf(@NotNull String islandName) {
        final Island island = islandManager.of(islandName);
        if(island == null)
            return Optional.empty();

        return Optional.ofNullable(island.getStatus());
    }
}

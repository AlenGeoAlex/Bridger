package io.github.alenalex.bridger.api.models;

import io.github.alenalex.bridger.api.IslandStatus;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public interface Island {

    String getIslandName();

    String getPermission();

    Location getSpawnLocation();

    Location getEndLocation();

    Location getPos1();

    Location getPos2();

    long getMinTimeRequired();

    int getMinBlocksRequired();

    boolean isEnabled();

    IslandStatus getStatus();

    boolean isIslandIdle();

    boolean isIslandOccupied();

    boolean isIslandResetting();

    World getIslandWorld();

    void teleportToSpawn(Player player);

    List<UUID> getSpectators();

    boolean canPlayerJoinTheIsland(@NotNull Player player);

    double getJoinCost();

    double getRewards();

    boolean hasPermission(@NotNull Player player);

}

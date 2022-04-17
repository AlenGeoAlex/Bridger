package io.github.alenalex.bridger.models;

import io.github.alenalex.bridger.variables.IslandStatus;
import io.github.alenalex.bridger.variables.Materials;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.IBlockData;
import net.minecraft.server.v1_8_R3.PlayerChunkMap;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public final class Island {

    public static final String PLACED_BLOCK = "placed_block";

    private final String islandName;
    private final String permission;
    private final Location spawnLocation;
    private final Location endLocation;
    private final Location pos1;
    private final Location pos2;
    private final int minTimeRequired;
    private final int minBlocksRequired;

    private boolean enabled;
    private IslandStatus status;

    public Island(@NotNull String islandName, String permission,@NotNull Location spawnLocation,@NotNull Location endLocation,@NotNull Location pos1,@NotNull Location pos2, int minTimeRequired, int minBlocksRequired) {
        this.islandName = islandName;
        this.permission = permission;
        this.spawnLocation = spawnLocation;
        this.endLocation = endLocation;
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.minTimeRequired = minTimeRequired;
        this.minBlocksRequired = minBlocksRequired;

        this.enabled = true;
        this.status = IslandStatus.IDLE;
    }

    public String getIslandName() {
        return islandName;
    }

    public String getPermission() {
        return permission;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public Location getEndLocation() {
        return endLocation;
    }

    public Location getPos1() {
        return pos1;
    }

    public Location getPos2() {
        return pos2;
    }

    public int getMinTimeRequired() {
        return minTimeRequired;
    }

    public int getMinBlocksRequired() {
        return minBlocksRequired;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public IslandStatus getStatus() {
        return status;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setResetting() {
        this.status = IslandStatus.RESETTING;
    }

    public void setOccupied() {
        this.status = IslandStatus.OCCUPIED;
    }

    public void setIdle() {
        this.status = IslandStatus.IDLE;
    }

    public boolean hasPassedRequirement(int placedBlocks, long timeTaken) {
        return placedBlocks > minBlocksRequired && TimeUnit.MILLISECONDS.toSeconds(timeTaken) > minTimeRequired;
    }

    public boolean hasPermission(@NotNull Player player) {
        if(StringUtils.isBlank(permission))
            return true;

        return player.hasPermission(permission);
    }

    public boolean isIslandIdle() {
        return status == IslandStatus.IDLE;
    }

    public boolean isIslandOccupied() {
        return status == IslandStatus.OCCUPIED;
    }

    public boolean isIslandResetting() {
        return status == IslandStatus.RESETTING;
    }

    public World getIslandWorld() {
        return spawnLocation.getWorld();
    }

    public void teleportToSpawn(Player player) {
        player.teleport(spawnLocation);
    }

    public CompletableFuture<Boolean> resetIsland() {
        return CompletableFuture.supplyAsync(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                setResetting();
                Bukkit.getLogger().info("The island " + islandName + " has been prepared for resetting.");
                getPossibleBlocksToRemove().thenAccept(blocks -> {
                    blocks.forEachRemaining(block -> {
                        net.minecraft.server.v1_8_R3.World nmsWorld = ((CraftWorld) getIslandWorld()).getHandle();
                        net.minecraft.server.v1_8_R3.Chunk nmsChunk = nmsWorld.getChunkAt(block.getX() >> 4, block.getZ() >> 4);
                        BlockPosition bp = new BlockPosition(block.getX(), block.getY(), block.getZ());
                        IBlockData ibd = net.minecraft.server.v1_8_R3.Block.getByCombinedId(0);
                        nmsChunk.a(bp, ibd);
                    });
                });

                for(Player player : getIslandWorld().getPlayers()) {
                    PlayerChunkMap playerChunkMap = ((CraftPlayer) player).getHandle().u().getPlayerChunkMap();
                    EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
                    playerChunkMap.removePlayer(entityPlayer);
                    playerChunkMap.addPlayer(entityPlayer);
                }

                setIdle();
                Bukkit.getLogger().info("The island " + islandName + " is now available for use.");
                return true;
            }
        });
    }

    public CompletableFuture<Iterator<Block>> getPossibleBlocksToRemove(){
        return CompletableFuture.supplyAsync(new Supplier<Iterator<Block>>() {
            @Override
            public Iterator<Block> get() {
                final int smallx = Math.min(pos1.getBlockX(),pos2.getBlockX());
                final int largex  = Math.max(pos1.getBlockX(),pos2.getBlockX());
                final int smally = Math.min(pos1.getBlockY(),pos2.getBlockY());
                final int largey = Math.max(pos1.getBlockY(),pos2.getBlockY());
                final int smallz = Math.min(pos1.getBlockZ(),pos2.getBlockZ());
                final int largez = Math.max(pos1.getBlockZ(),pos2.getBlockZ());

                final HashSet<Block> blocks = new HashSet<>();

                for(int x = smallx;x<=largex;x++){
                    for(int y = smally;y<=largey;y++){
                        for(int z = smallz;z<=largez;z++){
                            Location loc = new Location(getIslandWorld(),x,y,z);
                            final Block block = loc.getBlock();
                            //TODO add check for if block is a valid material.
                            //SO here it will check if it has a placed block meta data, but in case of restart it won't be useful, so if it doesn't have that,
                            //it will check whether its in list of enabled block

                            //if block#hasMetaData || block#isInListOfEnabledBlocks
                            if(block.hasMetadata(PLACED_BLOCK) || Materials.isMaterialTypeEnabled(block.getType()))
                                blocks.add(block);
                        }
                    }
                }
                return blocks.iterator();
            }
        });
    }

}

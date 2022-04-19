package io.github.alenalex.bridger.models.player;

import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.models.Island;
import io.github.alenalex.bridger.variables.Materials;
import io.github.alenalex.bridger.workload.core.Workload;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class UserMatchCache {
    private final UserData userData;
    private final List<Block> placedBlocks;
    private long startTime;
    private long currentTime;

    private String spectatingIsland;

    private Status status;

    public UserMatchCache(UserData userData) {
        this.userData = userData;
        this.placedBlocks = new ArrayList<>();
        this.startTime = 0L;
        this.currentTime = 0L;
        this.spectatingIsland = null;
        this.status = Status.LOBBY;
    }

    public enum Status {
        PLAYING,
        IDLE,
        LOBBY,
        SPECTATING
    }

    public void addBlockToCache(@NotNull Block block){
        block.setMetadata(Island.PLACED_BLOCK, new FixedMetadataValue(Bridger.instance(), 0));
        this.placedBlocks.add(block);
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    public void resetPlacedBlocks(){
        if(placedBlocks.isEmpty())
            return;

        for(Block block: placedBlocks){
            Workload workload = () -> {
                block.setType(Material.AIR);
                if(block.hasMetadata(Island.PLACED_BLOCK))
                    block.removeMetadata(Island.PLACED_BLOCK, Bridger.instance());
                if(userData.userSettings().hasParticle()){

                }
            };
            Bridger.instance().workloadHandler().getSyncThread().submit(workload);
        }
        userData.userStats().addBlock(placedBlocks.size());
        placedBlocks.clear();
    }

    public void forceResetPlacedBlocks(){
        if(placedBlocks.isEmpty())
            return;

        for(Block block: placedBlocks){
            Workload workload = () -> {
                block.setType(Material.AIR);
                if(block.hasMetadata(Island.PLACED_BLOCK))
                    block.removeMetadata(Island.PLACED_BLOCK, Bridger.instance());
            };
            Bridger.instance().workloadHandler().getSyncThread().submit(workload);
        }
        userData.userStats().addBlock(placedBlocks.size());
        placedBlocks.clear();
    }

    public String getSpectatingIsland() {
        return spectatingIsland;
    }

    public void setSpectatingIsland(String spectatingIsland) {
        this.status = Status.SPECTATING;
        this.spectatingIsland = spectatingIsland;
    }

    public void clearSpectatingIsland(){
        setSpectatingIsland(null);
        this.status = Status.LOBBY;
    }

    public boolean isUserSpectating(){
        return status == Status.SPECTATING;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public String getCurrentTimeAsString(){
        return String.valueOf(currentTime);
    }

    public void setPlayerAsIdle(){
        status = Status.IDLE;
    }

    public void setPlayerAsPlaying(){
        status = Status.PLAYING;
    }

    public void setPlayerAsLobby(){
        status = Status.LOBBY;
    }

    public Status getStatus() {
        return status;
    }
}

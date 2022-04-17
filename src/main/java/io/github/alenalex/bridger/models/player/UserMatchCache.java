package io.github.alenalex.bridger.models.player;

import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.variables.Materials;
import io.github.alenalex.bridger.workload.core.Workload;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class UserMatchCache {
    private final UserData userData;
    private final List<Block> placedBlocks;
    private long currentTime;


    public UserMatchCache(UserData userData) {
        this.userData = userData;
        this.placedBlocks = new ArrayList<>();
        this.currentTime = 0L;
    }

    public void addBlockToCache(@NotNull Block block){
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
                if(userData.userSettings().hasParticle()){

                }
            };
            Bridger.instance().workloadHandler().getSyncThread().submit(workload);
        }
        placedBlocks.clear();
    }

}

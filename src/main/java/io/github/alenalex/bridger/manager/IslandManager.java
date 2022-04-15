package io.github.alenalex.bridger.manager;

import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.abstracts.AbstractRegistry;
import io.github.alenalex.bridger.models.Island;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class IslandManager extends AbstractRegistry<String, Island> {

    public IslandManager(Bridger plugin) {
        super(plugin);
    }

    public void loadAllIslands(){

    }

    public List<Island> getFreeIslands(){
        return getValueStream().collect(Collectors.toList());
    }

    public Optional<Island> getAnyFreeIsland(){
        Island island = null;
        for(Island loopIsland : getValueCollection()){
            if(loopIsland.isIslandIdle() && loopIsland.isEnabled())
                island = loopIsland;
                break;
        }
        return Optional.ofNullable(island);
    }

    public List<Island> getAllOccupiedIsland(){
        return getValueStream().filter(Island::isIslandOccupied).collect(Collectors.toList());
    }

    public List<Island> getAllEnabledIsland(){
        return getValueStream().filter(Island::isEnabled).collect(Collectors.toList());
    }

    public void disableIsland(@NotNull String islandName){

    }

    public void enableIsland(){

    }

    public void reloadIsland(@NotNull String islandName){
        if(!isKeyRegistered(islandName))
            return;

        disableIsland(islandName);
        //TODO
    }




}

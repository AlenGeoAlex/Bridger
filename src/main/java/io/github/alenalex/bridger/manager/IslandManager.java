package io.github.alenalex.bridger.manager;

import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.abstracts.AbstractRegistry;
import io.github.alenalex.bridger.models.Island;

public class IslandManager extends AbstractRegistry<String, Island> {

    public IslandManager(Bridger plugin) {
        super(plugin);
    }

    public void loadAllIslands(){

    }
}

package io.github.alenalex.bridger.manager;

import io.github.alenalex.bridger.Bridger;

public class NPCManager {

    private final Bridger plugin;

    public NPCManager(Bridger plugin) {
        this.plugin = plugin;
    }

    public boolean initNPC(){
        return true;
    }

    public Bridger getPlugin() {
        return plugin;
    }
}

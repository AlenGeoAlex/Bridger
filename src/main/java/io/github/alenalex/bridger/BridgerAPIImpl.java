package io.github.alenalex.bridger;

import io.github.alenalex.bridger.api.manager.IslandManager;
import io.github.alenalex.bridger.api.manager.UserManager;

public class BridgerAPIImpl implements io.github.alenalex.bridger.api.BridgerAPI {

    private final Bridger plugin;

    public BridgerAPIImpl(Bridger plugin) {
        this.plugin = plugin;
    }

    @Override
    public IslandManager getIslandManager() {
        return plugin.gameHandler().islandManager();
    }

    @Override
    public UserManager getUserManager() {
        return plugin.gameHandler().userManager();
    }
}

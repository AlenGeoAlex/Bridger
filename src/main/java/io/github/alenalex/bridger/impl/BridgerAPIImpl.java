package io.github.alenalex.bridger.impl;

import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.api.BridgerAPI;
import io.github.alenalex.bridger.api.manager.IslandManager;
import io.github.alenalex.bridger.api.manager.PlayerManager;
import io.github.alenalex.bridger.manager.UserManager;
import org.jetbrains.annotations.NotNull;

public class BridgerAPIImpl implements BridgerAPI {

    private final Bridger plugin;
    private final PlayerManager playerManager;

    private boolean enabled = false;

    public BridgerAPIImpl(Bridger plugin) {
        this.plugin = plugin;
        this.playerManager = new PlayerManagerImpl(plugin.gameHandler().userManager());
    }

    @Override
    public boolean isReloading() {
        return Bridger.isPluginReloading();
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public @NotNull IslandManager getIslandManager() {
        return null;
    }

    @Override
    public @NotNull PlayerManager getPlayerManager() {
        return this.playerManager;
    }

    public void setAPIStatus(boolean status){
        this.enabled = status;
        if(status)
            plugin.getLogger().info("Plugin API has been enabled");
    }
}

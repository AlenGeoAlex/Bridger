package io.github.alenalex.bridger.impl;

import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.api.BridgerAPI;
import io.github.alenalex.bridger.api.manager.IslandManager;
import io.github.alenalex.bridger.api.manager.PlayerManager;
import org.jetbrains.annotations.NotNull;

public class BridgerAPIImpl implements BridgerAPI {

    private final Bridger plugin;

    private boolean enabled = false;

    public BridgerAPIImpl(Bridger plugin) {
        this.plugin = plugin;
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
        return null;
    }

    public void setAPIStatus(boolean status){
        this.enabled = status;
    }
}

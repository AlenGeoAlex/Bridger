package io.github.alenalex.bridger.implementation;

import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.api.BridgerAPI;
import io.github.alenalex.bridger.api.manager.IslandManager;
import io.github.alenalex.bridger.api.manager.PlayerManager;
import io.github.alenalex.bridger.models.Island;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public final class BridgerAPIImpl implements BridgerAPI {

    private final Bridger plugin;
    private final PlayerManager playerManager;
    private final IslandManager islandManager;

    private boolean enabled = false;

    public BridgerAPIImpl(Bridger plugin) {
        this.plugin = plugin;
        this.playerManager = new PlayerManagerImpl(plugin.gameHandler().userManager());
        this.islandManager = new IslandManagerImpl(plugin.gameHandler().islandManager());
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
        return islandManager;
    }

    @Override
    public @NotNull PlayerManager getPlayerManager() {
        return this.playerManager;
    }

    @Override
    public @NotNull Optional<Player> getPlayerOfIsland(@NotNull Island island) {
        return plugin.gameHandler().getPlayerOfIsland(island.getIslandName());
    }

    @Override
    public @NotNull Optional<Player> getPlayerOfIsland(@NotNull String islandName) {
        return plugin.gameHandler().getPlayerOfIsland(islandName);
    }

    public void setAPIStatus(boolean status){
        this.enabled = status;
        if(status)
            plugin.getLogger().info("Plugin API has been enabled");
    }
}

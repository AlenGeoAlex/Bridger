package io.github.alenalex.bridger.api;

import io.github.alenalex.bridger.api.manager.IslandManager;
import io.github.alenalex.bridger.api.manager.PlayerManager;
import io.github.alenalex.bridger.handler.ConfigurationHandler;
import io.github.alenalex.bridger.models.Island;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface BridgerAPI {

    /**
     *  Useful to know whether the plugin is reloading or not
     * @return is plugin reloading or not.
     */
    boolean isReloading();

    /**
     * Returns whether the plugin is enabled
     * @return is plugin enabled
     */
    boolean isEnabled();

    /**
     * Returns the Island Manager.
     * Island manager is useful for handling island related stuffs
     * @return IslandManager
     */
    @NotNull IslandManager getIslandManager();

    /**
     * Player Manager is useful for handing player related stuffs
     * @return PlayerManager
     */
    @NotNull PlayerManager getPlayerManager();

    /**
     * Gets the current player of the island
     * @param island island instance
     * @return player instance
     */
    @NotNull Optional<Player> getPlayerOfIsland(@NotNull Island island);

    /**
     * Gets the current player of the island
     * @param islandName name of the island
     * @return player instance
     */
    @NotNull Optional<Player> getPlayerOfIsland(@NotNull String islandName);

    /**
     * Get the instance of configuration handler
     * @return ConfigurationHandler
     */
    @NotNull ConfigurationHandler getConfigurationHandler();

}

package io.github.alenalex.bridger.api;

import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.api.manager.IslandManager;
import io.github.alenalex.bridger.api.manager.PlayerManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface BridgerAPI {

    /**
     * Gets the API instance of the plugin
     * @return BridgerAPI - The entry point for the plugin
     * @throws IllegalStateException If the api isn't yet initialized
     */
    @Nullable
    static BridgerAPI get() throws IllegalStateException{
        return Bridger.getAPIInstance();
    }

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

    @NotNull PlayerManager getPlayerManager();

}

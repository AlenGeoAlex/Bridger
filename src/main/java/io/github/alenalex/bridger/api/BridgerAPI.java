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

    boolean isReloading();

    boolean isEnabled();

    @NotNull IslandManager getIslandManager();

    @NotNull PlayerManager getPlayerManager();

}

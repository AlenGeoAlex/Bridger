package io.github.alenalex.bridger.api;

import io.github.alenalex.bridger.api.manager.IslandManager;
import io.github.alenalex.bridger.api.manager.UserManager;

public interface BridgerAPI {

    IslandManager getIslandManager();

    UserManager getUserManager();

}

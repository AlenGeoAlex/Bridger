package io.github.alenalex.bridger.api.models.player;

import java.util.Optional;

public interface UserMatchCache {

    Optional<String> getOptionalSpectatingIsland();

    boolean isUserSpectating();

    long getStartTime();

    long getCurrentTime();

    String getCurrentTimeAsString();

    int getBlocksPlaced();

    boolean isUserOnLobby();

    boolean isUserIdle();

    boolean isUserOnMatch();

}

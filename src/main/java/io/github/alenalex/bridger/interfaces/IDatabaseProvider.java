package io.github.alenalex.bridger.interfaces;

import io.github.alenalex.bridger.models.player.BridgerUserData;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface IDatabaseProvider {

    boolean connect();

    boolean isConnectionOpen();

    void closeConnection();

    boolean prepareDatabase();

    CompletableFuture<BridgerUserData> loadOrRegisterUser(@NotNull UUID uuid);

    void saveAllUserSync(@NotNull List<BridgerUserData> users);

    void saveUserAsync(@NotNull BridgerUserData user);

    void saveAllUsersAsync(@NotNull List<BridgerUserData> users);

}

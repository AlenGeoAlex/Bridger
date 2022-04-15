package io.github.alenalex.bridger.interfaces;

import io.github.alenalex.bridger.models.player.UserData;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface IDatabaseProvider {

    boolean connect();

    boolean isConnectionOpen();

    void closeConnection();

    boolean prepareDatabase();

    CompletableFuture<UserData> loadOrRegisterUser(@NotNull UUID uuid);

    void saveAllUserSync(@NotNull List<UserData> users);

    void saveUserAsync(@NotNull UserData user);

    void saveAllUsersAsync(@NotNull List<UserData> users);

}

package io.github.alenalex.bridger.impl;

import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.api.manager.PlayerManager;
import io.github.alenalex.bridger.manager.UserManager;
import io.github.alenalex.bridger.models.player.UserData;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public class PlayerManagerImpl implements PlayerManager {

    private final UserManager manager;

    public PlayerManagerImpl(@NotNull UserManager manager){
        this.manager = manager;
    }

    @Override
    public Optional<UserData> ofPlayer(@NotNull Player player) {
        return Optional.ofNullable(manager.of(player.getUniqueId()));
    }

    @Override
    public Optional<UserData> ofPlayer(@NotNull UUID uuid) {
        return Optional.ofNullable(manager.of(uuid));
    }

}

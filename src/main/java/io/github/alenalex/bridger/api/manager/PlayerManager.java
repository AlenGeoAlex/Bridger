package io.github.alenalex.bridger.api.manager;

import io.github.alenalex.bridger.models.player.UserData;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public interface PlayerManager {

    @NotNull Optional<UserData> ofPlayer(@NotNull Player player);

    @NotNull Optional<UserData> ofPlayer(@NotNull UUID uuid);

}

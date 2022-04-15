package io.github.alenalex.bridger.models.player;

import com.google.common.base.Objects;
import io.github.alenalex.bridger.Bridger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public final class UserData {

    @NotNull private final UUID playerUID;
    @NotNull private final UserStats userStats;
    @NotNull private final UserSettings userSettings;

    public UserData(@NotNull UUID playerUID, @NotNull UserStats userStats, @NotNull UserSettings userSettings) {
        this.playerUID = playerUID;
        this.userStats = userStats;
        this.userSettings = userSettings;
    }

    @NotNull
    public UUID getPlayerUID() {
        return playerUID;
    }

    @NotNull
    public Optional<Player> getPlayer() {
        return Optional.ofNullable(Bukkit.getPlayer(playerUID));
    }

    @NotNull
    public UserStats userStats() {
        return userStats;
    }

    @NotNull
    public UserSettings userSettings() {
        return userSettings;
    }

    @Nullable
    public Bridger getPlugin() {
        return Bridger.instance();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserData userData = (UserData) o;
        return Objects.equal(playerUID, userData.playerUID) && Objects.equal(userStats, userData.userStats) && Objects.equal(userSettings, userData.userSettings);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(playerUID, userStats, userSettings);
    }

    @Override
    public String toString() {
        return "UserData{" +
                "playerUID=" + playerUID.toString() +
                ", userStats=" + userStats.toString() +
                ", userSettings=" + userSettings.toString() +
                '}';
    }
}

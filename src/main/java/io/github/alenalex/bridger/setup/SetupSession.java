package io.github.alenalex.bridger.setup;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

public class SetupSession {

    private final UUID playerUID;
    private final String islandName;

    public SetupSession(UUID playerUID, String islandName) {
        this.playerUID = playerUID;
        this.islandName = islandName;
    }

    public Optional<Player> player(){
        return Optional.ofNullable(Bukkit.getPlayer(playerUID));
    }

    public String getIslandName(){
        return islandName;
    }

    public UUID getPlayerUID(){
        return playerUID;
    }
}

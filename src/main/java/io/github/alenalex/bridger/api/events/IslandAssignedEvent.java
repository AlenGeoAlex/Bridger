package io.github.alenalex.bridger.api.events;

import io.github.alenalex.bridger.models.Island;
import io.github.alenalex.bridger.models.player.UserData;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class IslandAssignedEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    private final Player player;
    private final UserData userData;
    private final Island island;
    private final boolean playerRequestedIsland;

    private boolean cancelled;

    public IslandAssignedEvent(Player player, UserData userData, Island island, boolean playerRequestedIsland) {
        this.player = player;
        this.userData = userData;
        this.island = island;
        this.playerRequestedIsland = playerRequestedIsland;
    }

    public Player getPlayer() {
        return player;
    }

    public UserData getUserData() {
        return userData;
    }

    public Island getIsland() {
        return island;
    }

    public boolean isPlayerRequestedIsland() {
        return playerRequestedIsland;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }
}

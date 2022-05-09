package io.github.alenalex.bridger.api.events;

import io.github.alenalex.bridger.models.player.UserData;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * This event is called when a player first placed a block on the island
 * This event is cancellable, if cancelled will not allow the first block to be placed
 */
public final class PlayerBridgingStartedEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    private final Player player;
    private final UserData userData;
    private boolean cancelled;

    public PlayerBridgingStartedEvent(Player player, UserData userData) {
        this.player = player;
        this.userData = userData;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

    public Player getPlayer() {
        return player;
    }

    public UserData getUserData() {
        return userData;
    }
}

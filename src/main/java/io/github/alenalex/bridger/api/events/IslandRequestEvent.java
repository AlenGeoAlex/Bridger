package io.github.alenalex.bridger.api.events;

import io.github.alenalex.bridger.models.player.UserData;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * This event is called when a player request an island.
 * This can be either through GUI or through command or just normal /island
 * This is a cancellable event
 */
public final class IslandRequestEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    private final Player player;
    private final UserData userData;

    public IslandRequestEvent(Player player, UserData userData) {
        this.player = player;
        this.userData = userData;
    }

    private boolean cancelled = false;

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public Player getPlayer() {
        return player;
    }

    public UserData getUserData() {
        return userData;
    }
}

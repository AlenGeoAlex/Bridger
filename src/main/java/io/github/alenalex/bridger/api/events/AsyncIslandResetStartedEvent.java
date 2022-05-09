package io.github.alenalex.bridger.api.events;

import io.github.alenalex.bridger.models.Island;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class AsyncIslandResetStartedEvent extends Event {
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    private final Island island;

    public AsyncIslandResetStartedEvent(Island island) {
        super(true);
        this.island = island;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }
}

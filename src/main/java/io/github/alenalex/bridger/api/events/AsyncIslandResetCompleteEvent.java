package io.github.alenalex.bridger.api.events;

import io.github.alenalex.bridger.models.Island;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class AsyncIslandResetCompleteEvent extends Event {

    private static final HandlerList HANDLERS_LIST = new HandlerList();
    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    private final Island island;
    private final long timeTook;

    public AsyncIslandResetCompleteEvent(Island island, long timeTook) {
        super(true);
        this.island = island;
        this.timeTook = timeTook;
    }

    public Island getIsland() {
        return island;
    }

    public long getTimeTook() {
        return timeTook;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }
}

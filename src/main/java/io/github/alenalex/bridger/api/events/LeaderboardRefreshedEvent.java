package io.github.alenalex.bridger.api.events;

import io.github.alenalex.bridger.models.leaderboard.LeaderboardPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.List;

/**
 * This event is called when a leaderboard refresh happens
 * This is not a cancellable event
 */
public final class LeaderboardRefreshedEvent extends Event {

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    private final List<LeaderboardPlayer> refreshedList;
    private final List<LeaderboardPlayer> oldList;

    public LeaderboardRefreshedEvent(List<LeaderboardPlayer> refreshedList, List<LeaderboardPlayer> oldList) {
        this.refreshedList = refreshedList;
        this.oldList = oldList;
    }

    public List<LeaderboardPlayer> getRefreshedList() {
        return refreshedList;
    }

    public List<LeaderboardPlayer> getOldList() {
        return oldList;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }
}

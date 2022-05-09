package io.github.alenalex.bridger.api.events;

import io.github.alenalex.bridger.models.Island;
import io.github.alenalex.bridger.models.player.UserData;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class PlayerPracticeFailedEvent extends Event {

    private static final HandlerList HANDLERS_LIST = new HandlerList();
    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    private final Player player;
    private final UserData userData;
    private final Island island;

    public PlayerPracticeFailedEvent(Player player, UserData userData, Island island) {
        this.player = player;
        this.userData = userData;
        this.island = island;
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

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }
}

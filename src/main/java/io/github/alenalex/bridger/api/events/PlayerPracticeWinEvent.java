package io.github.alenalex.bridger.api.events;

import io.github.alenalex.bridger.models.Island;
import io.github.alenalex.bridger.models.player.UserData;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class PlayerPracticeWinEvent extends Event {

    private static final HandlerList HANDLERS_LIST = new HandlerList();
    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    private final UserData userData;
    private final Player player;
    private final Island island;

    public PlayerPracticeWinEvent(UserData userData, Player player, Island island) {
        this.userData = userData;
        this.player = player;
        this.island = island;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public UserData getUserData() {
        return userData;
    }

    public Player getPlayer() {
        return player;
    }

    public Island getIsland() {
        return island;
    }
}

package io.github.alenalex.bridger.api.events;

import io.github.alenalex.bridger.models.player.UserData;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class UserDataLoadedEvent extends Event {
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    private final Player player;
    private final UserData userData;

    public UserDataLoadedEvent(Player player, UserData userData) {
        this.player = player;
        this.userData = userData;
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
}

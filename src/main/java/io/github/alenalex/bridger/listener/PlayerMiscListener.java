package io.github.alenalex.bridger.listener;

import io.github.alenalex.bridger.Bridger;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerMiscListener implements Listener {

    private final Bridger plugin;

    public PlayerMiscListener(Bridger plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerHungerEvent(){

    }
}

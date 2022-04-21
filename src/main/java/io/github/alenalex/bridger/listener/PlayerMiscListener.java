package io.github.alenalex.bridger.listener;

import io.github.alenalex.bridger.Bridger;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public final class PlayerMiscListener implements Listener {

    private final Bridger plugin;

    public PlayerMiscListener(Bridger plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerHungerEvent(FoodLevelChangeEvent event){
        if(event.isCancelled())
            return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onPrimeExplosionEvent(ExplosionPrimeEvent event){
        if(event.isCancelled()) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onCreatureSpawnEvent(CreatureSpawnEvent event){
        if(event.isCancelled()) return;

        if(event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.CUSTOM)
            return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onExplosion(BlockExplodeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        event.setCancelled(true);
    }
}

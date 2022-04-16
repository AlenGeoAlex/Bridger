package io.github.alenalex.bridger.models.player;

import com.google.common.base.Objects;
import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.variables.Fireworks;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.function.Consumer;

public final class UserData {

    @NotNull private final UUID playerUID;
    @NotNull private final UserStats userStats;
    @NotNull private final UserSettings userSettings;
    @NotNull private final UserCosmetics userCosmetics;

    public UserData(@NotNull UUID playerUID, @NotNull UserStats userStats, @NotNull UserSettings userSettings, @NotNull UserCosmetics userCosmetics) {
        this.playerUID = playerUID;
        this.userStats = userStats;
        this.userSettings = userSettings;
        this.userCosmetics = userCosmetics;
    }

    @NotNull
    public UUID getPlayerUID() {
        return playerUID;
    }

    @NotNull
    public Optional<Player> getPlayer() {
        return Optional.ofNullable(Bukkit.getPlayer(playerUID));
    }

    @NotNull
    public UserStats userStats() {
        return userStats;
    }

    @NotNull
    public UserSettings userSettings() {
        return userSettings;
    }

    public void doFireworksOnPlayerLocation (){
        final Player player = getPlayer().orElse(null);
        if(player == null || !player.isOnline()) return;

        if(!Bridger.instance().configurationHandler().getConfigurationFile().isFireworkEnabled())
            return;

        if(!userSettings.hasFireWork())
            return;

        final Optional<FireworkEffect.Type> type = userSettings.getFireWork();

        if(type.isPresent()){
            Bridger.instance().getServer().getScheduler().runTask(Bridger.instance(), () -> {
                Firework fw = (Firework) player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
                FireworkMeta fwm = fw.getFireworkMeta();
                Color c1 = Fireworks.getRandomColor();
                Color c2 = Fireworks.getRandomColor();
                Color c3 = Fireworks.getRandomColor();
                FireworkEffect effect = FireworkEffect.
                        builder().
                        withColor(c1, c3).
                        flicker(Bridger.randomInstance().nextBoolean()).
                        withFade(c2).
                        with(type.get()).
                        trail(Bridger.randomInstance().nextBoolean()).
                        build();
                fwm.addEffect(effect);
                int power = Bridger.randomInstance().nextInt(2) + 1;
                fwm.setPower(power);
                fw.setFireworkMeta(fwm);
            });
        }else {
            Bridger.instance().getLogger().warning("User " + player.getName() + " has an unknown firework type "+userSettings.getFireWorkAsString()+" selected as his firework. The plugin will try to remove it!");
            userSettings.setFireWork(null);
        }

    }

    @Nullable
    public Bridger getPlugin() {
        return Bridger.instance();
    }

    @NotNull
    public UserCosmetics userCosmetics() {
        return userCosmetics;
    }
}

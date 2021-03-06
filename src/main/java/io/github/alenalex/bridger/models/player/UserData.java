package io.github.alenalex.bridger.models.player;

import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.utils.FireworkUtils;
import io.github.alenalex.bridger.utils.ParticleUtils;
import io.github.alenalex.bridger.variables.LangConfigurationPaths;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.xenondevs.particle.ParticleEffect;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public final class UserData {

    @NotNull private final UUID playerUID;
    @NotNull private final UserStats userStats;
    @NotNull private final UserSettings userSettings;
    @NotNull private final UserCosmetics userCosmetics;
    @NotNull private final UserMatchCache userMatchCache;

    public UserData(@NotNull UUID playerUID, @NotNull UserStats userStats, @NotNull UserSettings userSettings, @NotNull UserCosmetics userCosmetics) {
        this.playerUID = playerUID;
        this.userStats = userStats;
        this.userSettings = userSettings;
        this.userCosmetics = userCosmetics;
        this.userMatchCache = new UserMatchCache(this);
    }

    @NotNull
    public UUID getPlayerUID() {
        return playerUID;
    }

    @NotNull
    public Optional<Player> getOptionalPlayer() {
        return Optional.ofNullable(Bukkit.getPlayer(playerUID));
    }

    @Nullable
    public Player getPlayer(){
        return Bukkit.getPlayer(playerUID);
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
        final Player player = getOptionalPlayer().orElse(null);
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
                Color c1 = FireworkUtils.getRandomColor();
                Color c2 = FireworkUtils.getRandomColor();
                Color c3 = FireworkUtils.getRandomColor();
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
            Bridger.instance().messagingUtils().sendTo(
                    player,
                    userSettings.getLanguage().asComponent(LangConfigurationPaths.UNABLE_TO_PROCESS_SELECTED_FIREWORK)
            );
            userSettings.setFireWork(null);
        }

    }

    public List<FireworkEffect.Type> fetchLockedFireworks(){
        return Bridger.instance()
                .configurationHandler()
                .getConfigurationFile()
                .getEnabledFireWorkModels()
                .keySet().
                stream()
                .filter(type -> {
                    boolean b = !userCosmetics.getFireWorkUnlocked().contains(type.name());
                    return b;
                }).
                collect(Collectors.toList());
    }

    public List<ItemStack> fetchLockedMaterials(){
        return Bridger.instance()
                .configurationHandler()
                .getConfigurationFile()
                .getEnabledMaterials()
                .keySet().
                stream()
                .filter(type -> {
                    boolean b = !userCosmetics.getMaterialUnlocked().contains(type.getType().name());
                    return b;
                }).
                collect(Collectors.toList());
    }

    public List<ParticleEffect> fetchLockedParticles(){
        return Bridger.instance()
                .configurationHandler()
                .getConfigurationFile()
                .getEnabledParticle()
                .keySet()
                .stream()
                .filter(type -> {
                    boolean b = !userCosmetics.getParticleUnlocked().contains(type.getFieldName());
                    return b;
                })
                .collect(Collectors.toList());
    }

    @Nullable
    public Bridger getPlugin() {
        return Bridger.instance();
    }

    @NotNull
    public UserCosmetics userCosmetics() {
        return userCosmetics;
    }

    @NotNull
    public UserMatchCache userMatchCache(){
        return userMatchCache;
    }

    public void setScoreboardOff(){
        if(!this.userSettings.isScoreboardEnabled())
            return;

        this.userSettings.setScoreboardEnabled(false);
        this.userMatchCache.deleteScoreboard();
    }

    public void setScoreboardOn(){
        if(this.userSettings.isScoreboardEnabled())
            return;

        this.userSettings.setScoreboardEnabled(true);
        this.userMatchCache.spawnScoreboard();
    }

    @Override
    public String toString() {
        return playerUID.toString();
    }

    public String asJson(){
        return Bridger.gsonInstance().toJson(this);
    }
}

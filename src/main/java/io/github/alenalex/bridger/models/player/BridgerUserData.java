package io.github.alenalex.bridger.models.player;

import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.api.models.player.UserData;
import io.github.alenalex.bridger.variables.Fireworks;
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

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public final class BridgerUserData implements UserData {

    @NotNull private final UUID playerUID;
    @NotNull private final BridgerUserStats bridgerUserStats;
    @NotNull private final BridgerUserSettings bridgerUserSettings;
    @NotNull private final BridgerUserCosmetics bridgerUserCosmetics;
    @NotNull private final BridgerUserMatchCache bridgerUserMatchCache;

    public BridgerUserData(@NotNull UUID playerUID, @NotNull BridgerUserStats bridgerUserStats, @NotNull BridgerUserSettings bridgerUserSettings, @NotNull BridgerUserCosmetics bridgerUserCosmetics) {
        this.playerUID = playerUID;
        this.bridgerUserStats = bridgerUserStats;
        this.bridgerUserSettings = bridgerUserSettings;
        this.bridgerUserCosmetics = bridgerUserCosmetics;
        this.bridgerUserMatchCache = new BridgerUserMatchCache(this);
    }

    @NotNull @Override
    public UUID getPlayerUID() {
        return playerUID;
    }

    @NotNull @Override
    public Optional<Player> getOptionalPlayer() {
        return Optional.ofNullable(Bukkit.getPlayer(playerUID));
    }

    @Nullable @Override
    public Player getPlayer(){
        return Bukkit.getPlayer(playerUID);
    }

    @NotNull @Override
    public BridgerUserStats userStats() {
        return bridgerUserStats;
    }

    @NotNull @Override
    public BridgerUserSettings userSettings() {
        return bridgerUserSettings;
    }

    public void doFireworksOnPlayerLocation (){
        final Player player = getOptionalPlayer().orElse(null);
        if(player == null || !player.isOnline()) return;

        if(!Bridger.instance().configurationHandler().getConfigurationFile().isFireworkEnabled())
            return;

        if(!bridgerUserSettings.hasFireWork())
            return;

        final Optional<FireworkEffect.Type> type = bridgerUserSettings.getFireWork();

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
            Bridger.instance().getLogger().warning("User " + player.getName() + " has an unknown firework type "+ bridgerUserSettings.getFireWorkAsString()+" selected as his firework. The plugin will try to remove it!");
            Bridger.instance().messagingUtils().sendTo(
                    player,
                    bridgerUserSettings.getLanguage().asComponent(LangConfigurationPaths.UNABLE_TO_PROCESS_SELECTED_FIREWORK)
            );
            bridgerUserSettings.setFireWork(null);
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
                    boolean b = !bridgerUserCosmetics.getFireWorkUnlocked().contains(type.name());
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
                    boolean b = !bridgerUserCosmetics.getMaterialUnlocked().contains(type.getType().name());
                    return b;
                }).
                collect(Collectors.toList());
    }

    @Nullable
    public Bridger getPlugin() {
        return Bridger.instance();
    }

    @NotNull @Override
    public BridgerUserCosmetics userCosmetics() {
        return bridgerUserCosmetics;
    }

    @NotNull @Override
    public BridgerUserMatchCache userMatchCache(){
        return bridgerUserMatchCache;
    }

    public void setScoreboardOff(){
        if(!this.bridgerUserSettings.isScoreboardEnabled())
            return;

        this.bridgerUserSettings.setScoreboardEnabled(false);
        this.bridgerUserMatchCache.deleteScoreboard();
    }

    public void setScoreboardOn(){
        if(this.bridgerUserSettings.isScoreboardEnabled())
            return;

        this.bridgerUserSettings.setScoreboardEnabled(true);
        this.bridgerUserMatchCache.spawnScoreboard();
    }

    @Override
    public String toString() {
        return playerUID.toString();
    }

    public String asJson(){
        return Bridger.gsonInstance().toJson(this);
    }
}

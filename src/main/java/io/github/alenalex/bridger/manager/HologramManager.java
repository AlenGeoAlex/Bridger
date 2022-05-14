package io.github.alenalex.bridger.manager;

import com.github.unldenis.hologram.Hologram;
import com.github.unldenis.hologram.HologramPool;
import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.interfaces.IHandler;
import io.github.alenalex.bridger.models.Island;
import io.github.alenalex.bridger.utils.FlatFileUtils;
import io.github.alenalex.bridger.utils.adventure.MessageFormatter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class HologramManager implements IHandler {

    private final Bridger plugin;
    private final HologramPool hologramPool;

    public HologramManager(Bridger plugin) {
        this.plugin = plugin;
        this.hologramPool = new HologramPool(plugin, 70);
    }

    @Override
    public boolean initHandler() {
        if(!plugin.getServer().getPluginManager().isPluginEnabled(HookManager.PROTOCOL_LIB)) {
            plugin.getLogger().warning(HookManager.PROTOCOL_LIB+" is disabled! #"+this.getClass().getName());
            return false;
        }

        for(Island island : plugin.gameHandler().islandManager().getValueCollection()) {
            enableIslandHologramOfIsland(island);
        }
        return true;
    }

    public void enableIslandHologramOfIsland(@NotNull Island island){
        if (plugin.configurationHandler().getHologramConfig().getSpawnPointHoloConfigData().isEnabled() && island.getSpawnHologram() == null) {
            final Location islandSpawnLocation = island.getSpawnLocation();
            final Location hologramSpawnLocation = islandSpawnLocation.clone();
            hologramSpawnLocation.setX(islandSpawnLocation.getX() + plugin.configurationHandler().getHologramConfig().getSpawnPointHoloConfigData().getOffSetX());
            hologramSpawnLocation.setY(islandSpawnLocation.getY() + plugin.configurationHandler().getHologramConfig().getSpawnPointHoloConfigData().getOffSetY());
            hologramSpawnLocation.setZ(islandSpawnLocation.getZ() + plugin.configurationHandler().getHologramConfig().getSpawnPointHoloConfigData().getOffSetZ());

            final Hologram.Builder builder = Hologram.builder();
            builder.location(hologramSpawnLocation);
            builder.addPlaceholder("%island-name%", new Function<Player, String>() {
                @Override
                public String apply(Player player) {
                    return island.getIslandName();
                }
            });
            builder.addPlaceholder("%player%", new Function<Player, String>() {
                @Override
                public String apply(Player player) {
                    return player.getName();
                }
            });

            for (String eachLine : plugin.configurationHandler().getHologramConfig().getSpawnPointHoloConfigData().getLines()) {
                if (eachLine.startsWith("[MATERIAL]:")) {
                    final String materialName = (String) eachLine.subSequence("[MATERIAL]:".length(), eachLine.length());
                    final ItemStack materialStack = FlatFileUtils.deserializeItemStack(materialName);
                    if (materialStack == null)
                        continue;

                    builder.addLine(materialStack);
                } else if (eachLine.startsWith("[MATERIAL-ROTATE]:")) {
                    final String materialName = (String) eachLine.subSequence("[MATERIAL-ROTATE]:".length(), eachLine.length());
                    final ItemStack materialStack = FlatFileUtils.deserializeItemStack(materialName);
                    if (materialStack == null)
                        continue;

                    builder.addLine(materialStack);
                } else {
                    builder.addLine(MessageFormatter.toLegacyString(MessageFormatter.transform(eachLine)));
                }
            }
            island.setSpawnHologram(builder.build(hologramPool));
        }

        if (plugin.configurationHandler().getHologramConfig().getEndPointHoloConfigData().isEnabled() && island.getEndHologram() == null) {
            final Location islandEndLocation = island.getEndLocation();
            final Location hologramEndLocation = islandEndLocation.clone();
            hologramEndLocation.setX(islandEndLocation.getX() + plugin.configurationHandler().getHologramConfig().getEndPointHoloConfigData().getOffSetX());
            hologramEndLocation.setY(islandEndLocation.getY() + plugin.configurationHandler().getHologramConfig().getEndPointHoloConfigData().getOffSetY());
            hologramEndLocation.setZ(islandEndLocation.getZ() + plugin.configurationHandler().getHologramConfig().getEndPointHoloConfigData().getOffSetZ());

            final Hologram.Builder builder = Hologram.builder();
            builder.location(hologramEndLocation);
            builder.addPlaceholder("%island-name%", new Function<Player, String>() {
                @Override
                public String apply(Player player) {
                    return island.getIslandName();
                }
            });
            builder.addPlaceholder("%player%", new Function<Player, String>() {
                @Override
                public String apply(Player player) {
                    return player.getName();
                }
            });

            for (String eachLine : plugin.configurationHandler().getHologramConfig().getEndPointHoloConfigData().getLines()) {
                if (eachLine.startsWith("[MATERIAL]:")) {
                    final String materialName = (String) eachLine.subSequence("[MATERIAL]:".length(), eachLine.length());
                    final ItemStack materialStack = FlatFileUtils.deserializeItemStack(materialName);
                    if (materialStack == null) {
                        plugin.getLogger().warning("The given material "+materialName+" seems to be invalid!");
                        plugin.getLogger().warning("That line would be skipped!");
                        continue;
                    }

                    builder.addLine(materialStack);
                } else if (eachLine.startsWith("[MATERIAL-ROTATE]:")) {
                    final String materialName = (String) eachLine.subSequence("[MATERIAL-ROTATE]:".length(), eachLine.length());
                    final ItemStack materialStack = FlatFileUtils.deserializeItemStack(materialName);
                    if (materialStack == null)
                        continue;

                    builder.addLine(materialStack);
                } else {
                    builder.addLine(MessageFormatter.toLegacyString(MessageFormatter.transform(eachLine)));
                }
            }
            island.setEndHologram(builder.build(hologramPool));

        }
    }

    public void removeHologramOfIsland(@NotNull Island island){
        if(island.getSpawnHologram() != null){
            this.hologramPool.remove(island.getSpawnHologram());
            island.setSpawnHologram(null);
        }
        if(island.getEndHologram() != null){
            this.hologramPool.remove(island.getEndHologram());
            island.setEndHologram(null);
        }
    }

    public void updateLeaderboardHologram(){

    }



    @Override
    public void prepareHandler() {

    }

    @Override
    public Bridger plugin() {
        return plugin;
    }

    public HologramPool getHologramPool() {
        return hologramPool;
    }
}

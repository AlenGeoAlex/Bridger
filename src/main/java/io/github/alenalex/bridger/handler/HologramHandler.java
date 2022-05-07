package io.github.alenalex.bridger.handler;

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

import java.util.function.Function;

public class HologramHandler implements IHandler {

    private final Bridger plugin;
    private final HologramPool hologramPool;

    public HologramHandler(Bridger plugin) {
        this.plugin = plugin;
        this.hologramPool = new HologramPool(plugin, 70);
    }

    @Override
    public boolean initHandler() {
        for(Island island : plugin.gameHandler().islandManager().getValueCollection()) {
            final Location islandSpawnLocation = island.getSpawnLocation();

            if (plugin.configurationHandler().getHologramConfig().getSpawnPointHoloData().isEnabled()) {
                final Location hologramSpawnLocation = islandSpawnLocation.clone();
                hologramSpawnLocation.setX(islandSpawnLocation.getX() + plugin.configurationHandler().getHologramConfig().getSpawnPointHoloData().getOffSetX());
                hologramSpawnLocation.setY(islandSpawnLocation.getY() + plugin.configurationHandler().getHologramConfig().getSpawnPointHoloData().getOffSetY());
                hologramSpawnLocation.setZ(islandSpawnLocation.getZ() + plugin.configurationHandler().getHologramConfig().getSpawnPointHoloData().getOffSetZ());

                final Hologram.Builder builder = Hologram.builder();
                builder.location(hologramSpawnLocation);
                builder.addPlaceholder("%island-name%", new Function<Player, String>() {
                    @Override
                    public String apply(Player player) {
                        return island.getIslandName();
                    }
                });

                for (String eachLine : plugin.configurationHandler().getHologramConfig().getSpawnPointHoloData().getLines()) {
                    if (eachLine.startsWith("[MATERIAL]:")) {
                        final String materialName = (String) eachLine.subSequence("[MATERIAL]:".length() - 1, eachLine.length() - 1);
                        final ItemStack materialStack = FlatFileUtils.deserializeItemStack(materialName);
                        if (materialStack == null)
                            continue;

                        builder.addLine(materialStack);
                    } else if (eachLine.startsWith("[MATERIAL-ROTATE]:")) {
                        final String materialName = (String) eachLine.subSequence("[MATERIAL-ROTATE]:".length() - 1, eachLine.length() - 1);
                        final ItemStack materialStack = FlatFileUtils.deserializeItemStack(materialName);
                        if (materialStack == null)
                            continue;

                        builder.addLine(materialStack);
                    } else {
                        builder.addLine(MessageFormatter.toLegacyString(MessageFormatter.transform(eachLine)));
                    }
                }
                builder.build(hologramPool);
            }

            if (plugin.configurationHandler().getHologramConfig().getEndPointHoloData().isEnabled()) {
                final Location hologramEndLocation = islandSpawnLocation.clone();
                hologramEndLocation.setX(islandSpawnLocation.getX() + plugin.configurationHandler().getHologramConfig().getEndPointHoloData().getOffSetX());
                hologramEndLocation.setY(islandSpawnLocation.getY() + plugin.configurationHandler().getHologramConfig().getEndPointHoloData().getOffSetY());
                hologramEndLocation.setZ(islandSpawnLocation.getZ() + plugin.configurationHandler().getHologramConfig().getEndPointHoloData().getOffSetZ());

                final Hologram.Builder builder = Hologram.builder();
                builder.location(hologramEndLocation);
                for (String eachLine : plugin.configurationHandler().getHologramConfig().getEndPointHoloData().getLines()) {
                    if (eachLine.startsWith("[MATERIAL]:")) {
                        final String materialName = (String) eachLine.subSequence("[MATERIAL]:".length() - 1, eachLine.length() - 1);
                        final ItemStack materialStack = FlatFileUtils.deserializeItemStack(materialName);
                        if (materialStack == null)
                            continue;

                        builder.addLine(materialStack);
                    } else if (eachLine.startsWith("[MATERIAL-ROTATE]:")) {
                        final String materialName = (String) eachLine.subSequence("[MATERIAL-ROTATE]:".length() - 1, eachLine.length() - 1);
                        final ItemStack materialStack = FlatFileUtils.deserializeItemStack(materialName);
                        if (materialStack == null)
                            continue;

                        builder.addLine(materialStack);
                    } else {
                        builder.addLine(MessageFormatter.toLegacyString(MessageFormatter.transform(eachLine)));
                    }
                    builder.build(hologramPool);
                }
            }
        }
        return false;
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

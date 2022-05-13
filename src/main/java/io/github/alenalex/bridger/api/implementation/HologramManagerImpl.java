package io.github.alenalex.bridger.api.implementation;

import com.github.unldenis.hologram.Hologram;
import io.github.alenalex.bridger.api.manager.HologramManager;
import io.github.alenalex.bridger.models.Island;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class HologramManagerImpl implements HologramManager {

    private final io.github.alenalex.bridger.manager.HologramManager manager;

    public HologramManagerImpl(io.github.alenalex.bridger.manager.HologramManager manager) {
        this.manager = manager;
    }

    @Override
    public void enableHologramOfIsland(@NotNull Island island) {
        manager.enableIslandHologramOfIsland(island);
    }

    @Override
    public void disableHologramOfIsland(@NotNull Island island) {
        manager.removeHologramOfIsland(island);
    }

    @Override
    public Hologram createHologram(@NotNull Location location, @NotNull Collection<String> strings) {
        final Hologram.Builder builder = Hologram.builder()
                .location(location);

        for(String eachLine : strings){
            builder.addLine(eachLine);
        }
        return builder.build(manager.getHologramPool());
    }

    @Override
    public void deleteHolograms(@NotNull Hologram hologram) {
        manager.getHologramPool().remove(hologram);
    }
}

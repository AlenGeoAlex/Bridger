package io.github.alenalex.bridger.api.manager;

import com.github.unldenis.hologram.Hologram;
import io.github.alenalex.bridger.models.Island;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface HologramManager {

    void enableHologramOfIsland(@NotNull Island island);

    void disableHologramOfIsland(@NotNull Island island);

    Hologram createHologram(@NotNull Location location, @NotNull Collection<String> strings);

    void deleteHolograms(@NotNull Hologram hologram);

}

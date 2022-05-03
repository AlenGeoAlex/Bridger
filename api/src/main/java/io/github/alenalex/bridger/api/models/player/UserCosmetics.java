package io.github.alenalex.bridger.api.models.player;

import org.jetbrains.annotations.NotNull;

public interface UserCosmetics {

    boolean isFireworkUnlocked(@NotNull String fireworkName);

    boolean isMaterialUnlocked(@NotNull String materialName);

    boolean isParticleUnlocked(@NotNull String particleName);

    void resetFireworks();

    void resetMaterials();

    void resetParticles();

    boolean isUnlockAllEnabled();
}

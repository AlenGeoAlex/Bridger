package io.github.alenalex.bridger.api.models.player;

import org.bukkit.inventory.ItemStack;

public interface UserSettings {

    String getLanguageAsString();

    String getMaterialAsString();

    ItemStack getCurrentBlock();

    String getCurrentBlockAsString();

    boolean hasMaterial();

    String getParticleAsString();

    boolean hasFireWork();

    String getFireWorkAsString();

    boolean hasParticle();

    boolean isScoreboardEnabled();

    boolean isSetBackEnabled();
}

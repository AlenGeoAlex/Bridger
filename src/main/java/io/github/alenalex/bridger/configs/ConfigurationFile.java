package io.github.alenalex.bridger.configs;

import io.github.alenalex.bridger.abstracts.AbstractFileSettings;
import io.github.alenalex.bridger.handler.ConfigurationHandler;
import io.github.alenalex.bridger.variables.ConfigurationPaths;
import org.apache.commons.lang3.EnumUtils;
import org.bukkit.Bukkit;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Firework;

import java.util.List;
import java.util.Optional;

public class ConfigurationFile extends AbstractFileSettings {

    private String storageType;
    private Location spawnLocation;

    private boolean isFireworkEnabled;
    private List<FireworkEffect.Type> allowedFireworkModels;

    public ConfigurationFile(ConfigurationHandler handler) {
        super(handler);
    }

    @Override
    public void loadFile() {
        this.storageType = this.file.getString(ConfigurationPaths.STORAGE_TYPE.getPath());
        Optional<Location> optionalSpawnLocation = deserializeLocation(ConfigurationPaths.SPAWN_LOCATION.getPath());
        spawnLocation = optionalSpawnLocation.orElseGet(() -> handler.plugin().getServer().getWorlds().get(0).getSpawnLocation());

        this.isFireworkEnabled = this.file.getBoolean(ConfigurationPaths.COSMETICS_FIRE_WORK_ENABLED.getPath());
        for(String s : this.file.getStringList(ConfigurationPaths.COSMETICS_FIRE_WORK_ENABLED.getPath())) {
            if(EnumUtils.isValidEnum(FireworkEffect.Type.class, s)) {
                allowedFireworkModels.add(FireworkEffect.Type.valueOf(s));
            }
        }
    }

    @Override
    public void prepareReload() {

    }

    public String getStorageType() {
        return storageType;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public boolean isFireworkEnabled() {
        return isFireworkEnabled;
    }

    public List<FireworkEffect.Type> getAllowedFireworkModels() {
        return allowedFireworkModels;
    }
}

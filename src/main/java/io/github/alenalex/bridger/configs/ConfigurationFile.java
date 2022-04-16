package io.github.alenalex.bridger.configs;

import io.github.alenalex.bridger.abstracts.AbstractFileSettings;
import io.github.alenalex.bridger.handler.ConfigurationHandler;
import io.github.alenalex.bridger.variables.ConfigurationPaths;
import org.apache.commons.lang3.EnumUtils;
import org.bukkit.Bukkit;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Firework;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ConfigurationFile extends AbstractFileSettings {

    private String storageType;
    private Location spawnLocation;

    private boolean isFireworkEnabled;

    private HashMap<FireworkEffect.Type, Integer> enabledFireworkModels;

    public ConfigurationFile(ConfigurationHandler handler) {
        super(handler);
    }

    @Override
    public void loadFile() {
        this.storageType = this.file.getString(ConfigurationPaths.STORAGE_TYPE.getPath());
        Optional<Location> optionalSpawnLocation = deserializeLocation(ConfigurationPaths.SPAWN_LOCATION.getPath());
        spawnLocation = optionalSpawnLocation.orElseGet(() -> handler.plugin().getServer().getWorlds().get(0).getSpawnLocation());

        this.isFireworkEnabled = this.file.getBoolean(ConfigurationPaths.COSMETICS_FIRE_WORK_ENABLED.getPath());

        for(String s : this.file.keySet(ConfigurationPaths.COSMETICS_ALLOWED_FIREWORK_MODELS.getPath())){
            if(EnumUtils.isValidEnum(FireworkEffect.Type.class, s)) {
                enabledFireworkModels.put(FireworkEffect.Type.valueOf(s), this.file.getInt(ConfigurationPaths.COSMETICS_ALLOWED_FIREWORK_MODELS.getPath() + "." + s));
            }else {
                handler.plugin().getLogger().warning("Invalid firework model: " + s+" found in config.yml");
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

    public Map<FireworkEffect.Type, Integer> getEnabledFireWorkModels() {
        return enabledFireworkModels;
    }
}

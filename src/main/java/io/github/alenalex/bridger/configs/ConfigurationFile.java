package io.github.alenalex.bridger.configs;

import io.github.alenalex.bridger.abstracts.AbstractFileSettings;
import io.github.alenalex.bridger.handler.ConfigurationHandler;
import io.github.alenalex.bridger.variables.ConfigurationPaths;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.Optional;

public class ConfigurationFile extends AbstractFileSettings {

    private String storageType;
    private Location spawnLocation;

    public ConfigurationFile(ConfigurationHandler handler) {
        super(handler);
    }

    @Override
    public void loadFile() {
        this.storageType = this.file.getString(ConfigurationPaths.STORAGE_TYPE.getPath());
        Optional<Location> optionalSpawnLocation = deserializeLocation(ConfigurationPaths.SPAWN_LOCATION.getPath());
        spawnLocation = optionalSpawnLocation.orElseGet(() -> handler.plugin().getServer().getWorlds().get(0).getSpawnLocation());
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
}

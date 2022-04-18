package io.github.alenalex.bridger.configs;

import io.github.alenalex.bridger.abstracts.AbstractFileSettings;
import io.github.alenalex.bridger.handler.ConfigurationHandler;
import io.github.alenalex.bridger.utils.FlatFileUtils;
import io.github.alenalex.bridger.variables.ConfigurationPaths;
import org.apache.commons.lang3.EnumUtils;
import org.bukkit.Bukkit;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class ConfigurationFile extends AbstractFileSettings {

    private String storageType;
    private Location spawnLocation;

    private boolean isFireworkEnabled;

    private final Map<FireworkEffect.Type, Integer> enabledFireworkModels;
    private ItemStack defaultMaterial;
    private final Map<ItemStack, Integer> enabledMaterials;

    private boolean broadcastNewBestTimeToAllPlayersEnabled;
    private boolean doAllowPlacingBlocksOnLobby;
    private boolean doAllowBreakingBlocksOnLobby;

    public ConfigurationFile(ConfigurationHandler handler) {
        super(handler);
        this.enabledFireworkModels = new HashMap<>();
        this.enabledMaterials = new HashMap<>();
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


        defaultMaterial = deserializeItemStack(ConfigurationPaths.COSMETICS_DEFAULT_MATERIAL.getPath()).orElseGet(new Supplier<ItemStack>() {
            @Override
            public ItemStack get() {
                handler.plugin().getLogger().warning("Default material is missing! Setting to STONE");
                return new ItemStack(Material.STONE);
            }
        });

        for(String s : this.file.keySet(ConfigurationPaths.COSMETICS_MATERIALS_ENABLED.getPath())){
            final ItemStack stack = FlatFileUtils.deserializeItemStack(s);
            if(stack == null) {
                handler.plugin().getLogger().warning("Failed to deserialize ItemStack on the object "+s);
                return;
            }

            enabledMaterials.put(stack, this.file.getInt(ConfigurationPaths.COSMETICS_MATERIALS_ENABLED.getPath() +"."+s));
        }

        this.broadcastNewBestTimeToAllPlayersEnabled = this.file.getBoolean(ConfigurationPaths.BROADCAST_NEW_RECORD.getPath());
        this.doAllowBreakingBlocksOnLobby = this.file.getBoolean(ConfigurationPaths.ALLOW_BREAKING_BLOCK_ON_LOBBY.getPath());
        this.doAllowPlacingBlocksOnLobby = this.file.getBoolean(ConfigurationPaths.ALLOW_PLACING_BLOCK_ON_LOBBY.getPath());
    }

    @Override
    public void prepareReload() {
        this.enabledMaterials.clear();
        this.enabledFireworkModels.clear();
        this.loadFile();
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

    public ItemStack getDefaultMaterial() {
        return defaultMaterial;
    }

    public Map<ItemStack, Integer> getEnabledMaterials() {
        return enabledMaterials;
    }

    public boolean isBroadcastNewBestTimeToAllPlayersEnabled() {
        return broadcastNewBestTimeToAllPlayersEnabled;
    }

    public boolean isDoAllowPlacingBlocksOnLobby() {
        return doAllowPlacingBlocksOnLobby;
    }

    public boolean isDoAllowBreakingBlocksOnLobby() {
        return doAllowBreakingBlocksOnLobby;
    }
}

package io.github.alenalex.bridger.configs;

import io.github.alenalex.bridger.abstracts.AbstractFileSettings;
import io.github.alenalex.bridger.gui.config.HotBarConfig;
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

import java.util.*;
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

    private final List<String> commandToBlock;

    private int voidDetectionHeight;
    private boolean voidDetectionOnLobbyEnabled;
    private boolean voidDetectionWhileSpectatingEnabled;

    private final List<Material> placementBlockedMaterial;

    private boolean cheatDetectionMinBlocks, cheatDetectionMinTime, cheatDetectionIdleCompletion;

    private HotBarConfig lobbySettings, lobbyShop, lobbyJoin, lobbySelector;
    private List<HotBarConfig> lobbyOther;

    public ConfigurationFile(ConfigurationHandler handler) {
        super(handler);
        this.enabledFireworkModels = new HashMap<>();
        this.enabledMaterials = new HashMap<>();
        this.commandToBlock = new ArrayList<>();
        this.placementBlockedMaterial = new ArrayList<>();
        this.lobbyOther = new ArrayList<>();
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

        this.voidDetectionHeight = this.file.getInt(ConfigurationPaths.VOID_DETECTION_HEIGHT.getPath());
        this.voidDetectionOnLobbyEnabled = this.file.getBoolean(ConfigurationPaths.DETECT_VOID_FALL_ON_LOBBY.getPath());
        this.voidDetectionWhileSpectatingEnabled = this.file.getBoolean(ConfigurationPaths.DETECT_VOID_FALL_WHILE_SPECTATOR.getPath());

        for(String s : this.file.getStringList(ConfigurationPaths.PLACEMENT_BLOCKED_MATERIALS.getPath())){
            if(!EnumUtils.isValidEnum(Material.class, s)){
                handler.plugin().getLogger().warning("An unknown material has been found in "+ConfigurationPaths.PLACEMENT_BLOCKED_MATERIALS.getPath()+".. Skipping!");
                continue;
            }

            this.placementBlockedMaterial.add(Material.getMaterial(s));
        }

        this.cheatDetectionMinBlocks = this.file.getBoolean(ConfigurationPaths.CHEAT_PROTECTION_MIN_BLOCK.getPath());
        this.cheatDetectionMinTime = this.file.getBoolean(ConfigurationPaths.CHEAT_PROTECTION_MIN_TIME.getPath());
        this.cheatDetectionIdleCompletion = this.file.getBoolean(ConfigurationPaths.CHEAT_PROTECTION_REACHED_IN_IDLE.getPath());

        this.lobbyJoin = HotBarConfig.of(getSectionOf(ConfigurationPaths.LOBBY_JOIN.getPath()));
        this.lobbySettings = HotBarConfig.of(getSectionOf(ConfigurationPaths.LOBBY_SETTINGS.getPath()));
        this.lobbyShop = HotBarConfig.of(getSectionOf(ConfigurationPaths.LOBBY_SHOP.getPath()));
        this.lobbySelector = HotBarConfig.of(getSectionOf(ConfigurationPaths.LOBBY_SELECTOR.getPath()));

        for(String s : this.file.keySet(ConfigurationPaths.LOBBY_OTHERS.getPath())){
            final HotBarConfig config = HotBarConfig.of(getSectionOf(ConfigurationPaths.LOBBY_OTHERS.getPath()+"."+s));
            this.lobbyOther.add(config);
        }
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

    public List<String> getCommandToBlock() {
        return commandToBlock;
    }

    public int getVoidDetectionHeight() {
        return voidDetectionHeight;
    }

    public boolean isVoidDetectionOnLobbyEnabled() {
        return voidDetectionOnLobbyEnabled;
    }

    public boolean isVoidDetectionWhileSpectatingEnabled() {
        return voidDetectionWhileSpectatingEnabled;
    }

    public List<Material> getPlacementBlockedMaterial() {
        return placementBlockedMaterial;
    }

    public boolean isCheatDetectionMinBlocks() {
        return cheatDetectionMinBlocks;
    }

    public boolean isCheatDetectionMinTime() {
        return cheatDetectionMinTime;
    }

    public boolean isCheatDetectionIdleCompletion() {
        return cheatDetectionIdleCompletion;
    }

    public HotBarConfig getLobbySettings() {
        return lobbySettings;
    }

    public HotBarConfig getLobbyShop() {
        return lobbyShop;
    }

    public HotBarConfig getLobbyJoin() {
        return lobbyJoin;
    }

    public HotBarConfig getLobbySelector() {
        return lobbySelector;
    }

    public List<HotBarConfig> getLobbyOther() {
        return lobbyOther;
    }
}

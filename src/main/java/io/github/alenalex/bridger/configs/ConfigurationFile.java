package io.github.alenalex.bridger.configs;

import io.github.alenalex.bridger.abstracts.AbstractFileSettings;
import io.github.alenalex.bridger.commands.config.CommandInfoConfig;
import io.github.alenalex.bridger.ui.config.HotBarConfig;
import io.github.alenalex.bridger.handler.ConfigurationHandler;
import io.github.alenalex.bridger.utils.FlatFileUtils;
import io.github.alenalex.bridger.utils.ParticleUtils;
import io.github.alenalex.bridger.utils.adventure.MessageFormatter;
import io.github.alenalex.bridger.variables.ConfigurationPaths;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import xyz.xenondevs.particle.ParticleEffect;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class ConfigurationFile extends AbstractFileSettings {

    private String storageType;
    private Location spawnLocation;

    private boolean isFireworkEnabled;

    private final Map<FireworkEffect.Type, Integer> enabledFireworkModels;
    private ItemStack defaultMaterial;
    private final Map<ItemStack, Integer> enabledMaterials;
    private boolean particleEnabled;
    private final Map<ParticleEffect, Integer> enabledParticle;

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
    private final List<HotBarConfig> lobbyOther;

    private int actionBarUpdateTime;

    private String serverJoinMessage;
    private String serverLeaveMessage;

    private int blockCount;
    private HotBarConfig matchLeaveItem;

    private CommandInfoConfig islandCommand, leaderboardCommand, leaveCommand, scoreboardCommand, setBackCommand, shopCommand;

    public ConfigurationFile(ConfigurationHandler handler) {
        super(handler);
        this.enabledFireworkModels = new HashMap<>();
        this.enabledMaterials = new HashMap<>();
        this.commandToBlock = new ArrayList<>();
        this.placementBlockedMaterial = new ArrayList<>();
        this.lobbyOther = new ArrayList<>();
        this.enabledParticle = new HashMap<>();
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

        this.actionBarUpdateTime = this.file.getInt(ConfigurationPaths.ACTION_BAR_UPDATE_RATE.getPath());
        this.actionBarUpdateTime = this.actionBarUpdateTime <= 99 ? 100 : this.actionBarUpdateTime;

        this.serverJoinMessage = this.file.getString(ConfigurationPaths.SERVER_JOIN_MESSAGE.getPath());
        this.serverLeaveMessage = this.file.getString(ConfigurationPaths.SERVER_LEAVE_MESSAGE.getPath());

        this.serverJoinMessage = StringUtils.isBlank(serverJoinMessage) ? null : MessageFormatter.colorizeLegacy(this.serverJoinMessage);
        this.serverLeaveMessage = StringUtils.isBlank(serverLeaveMessage) ? null : MessageFormatter.colorizeLegacy(this.serverLeaveMessage);

        this.blockCount = this.file.getInt(ConfigurationPaths.BLOCK_COUNT_MATCH.getPath());
        this.matchLeaveItem = HotBarConfig.of(getSectionOf(ConfigurationPaths.MATCH_LEAVE_GAME_ITEM.getPath()));

        this.particleEnabled = this.file.getBoolean(ConfigurationPaths.PARTICLE_ENABLED.getPath());
        for(String s : this.file.keySet(ConfigurationPaths.ALL_ENABLED_PARTICLES.getPath())){
            Optional<ParticleEffect> any = ParticleEffect
                    .getAvailableEffects()
                    .stream()
                    .filter(particleEffect -> particleEffect.getFieldName().equals(s))
                    .findAny();

            any.ifPresent(new Consumer<ParticleEffect>() {
                @Override
                public void accept(ParticleEffect particleEffect) {
                    ParticleUtils.registerParticle(particleEffect);
                    enabledParticle.put(particleEffect, file.getInt(ConfigurationPaths.PARTICLE_ENABLED.getPath()+"."+s));
                }
            });
        }

        this.islandCommand = CommandInfoConfig.buildFrom(getSectionOf(ConfigurationPaths.ISLAND_COMMAND.getPath()));
        this.setBackCommand = CommandInfoConfig.buildFrom(getSectionOf(ConfigurationPaths.SETBACK_COMMAND.getPath()));
        this.leaveCommand = CommandInfoConfig.buildFrom(getSectionOf(ConfigurationPaths.LEAVE_COMMAND.getPath()));
        this.scoreboardCommand = CommandInfoConfig.buildFrom(getSectionOf(ConfigurationPaths.SCOREBOARD_COMMAND.getPath()));
        this.shopCommand = CommandInfoConfig.buildFrom(getSectionOf(ConfigurationPaths.SHOP_COMMAND.getPath()));
    }

    @Override
    public void prepareReload() {
        this.enabledMaterials.clear();
        this.enabledFireworkModels.clear();
        this.commandToBlock.clear();
        this.placementBlockedMaterial.clear();
        this.lobbyOther.clear();
        this.enabledParticle.clear();
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

    public int getActionBarUpdateTime() {
        return actionBarUpdateTime;
    }

    public String getServerJoinMessage() {
        return serverJoinMessage;
    }

    public String getServerLeaveMessage() {
        return serverLeaveMessage;
    }

    public int getBlockCount() {
        return blockCount;
    }

    public HotBarConfig getMatchLeaveItem() {
        return matchLeaveItem;
    }

    public boolean isParticleEnabled() {
        return particleEnabled;
    }

    public Map<ParticleEffect, Integer> getEnabledParticle() {
        return enabledParticle;
    }

    public CommandInfoConfig getIslandCommand() {
        return islandCommand;
    }

    public CommandInfoConfig getLeaderboardCommand() {
        return leaderboardCommand;
    }

    public CommandInfoConfig getLeaveCommand() {
        return leaveCommand;
    }

    public CommandInfoConfig getScoreboardCommand() {
        return scoreboardCommand;
    }

    public CommandInfoConfig getSetBackCommand() {
        return setBackCommand;
    }

    public CommandInfoConfig getShopCommand() {
        return shopCommand;
    }
}

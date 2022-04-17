package io.github.alenalex.bridger.models.setup;

import io.github.alenalex.bridger.models.Island;
import io.github.alenalex.bridger.utils.FlatFileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class SetupSession {

    private final UUID playerUID;
    private final String islandName;

    private Location pos1, pos2, spawnPoint, endPoint;
    private int minBlocksRequired = 0;
    private int minSecRequired = 0;
    private String permissionRequired = null;

    public SetupSession(UUID playerUID, String islandName) {
        this.playerUID = playerUID;
        this.islandName = islandName;
    }

    public Optional<Player> player(){
        return Optional.ofNullable(Bukkit.getPlayer(playerUID));
    }

    public String getIslandName(){
        return islandName;
    }

    public UUID getPlayerUID(){
        return playerUID;
    }

    public Location getPos1() {
        return pos1;
    }

    public Location getPos2() {
        return pos2;
    }

    public Location getSpawnPoint() {
        return spawnPoint;
    }

    public Location getEndPoint() {
        return endPoint;
    }

    public int getMinBlocksRequired() {
        return minBlocksRequired;
    }

    public int getMinSecRequired() {
        return minSecRequired;
    }

    public String getPermissionRequired() {
        return permissionRequired;
    }

    public SetupSession setPos1(Location pos1) {
        this.pos1 = pos1;
        return this;
    }

    public SetupSession setPos2(Location pos2) {
        this.pos2 = pos2;
        return this;
    }

    public SetupSession setSpawnPoint(Location spawnPoint) {
        this.spawnPoint = spawnPoint;
        return this;
    }

    public SetupSession setEndPoint(Location endPoint) {
        this.endPoint = endPoint;
        return this;
    }

    public SetupSession setMinBlocksRequired(int minBlocksRequired) {
        this.minBlocksRequired = minBlocksRequired;
        return this;
    }

    public SetupSession setMinSecRequired(int minSecRequired) {
        this.minSecRequired = minSecRequired;
        return this;
    }

    public SetupSession setPermissionRequired(String permissionRequired) {
        this.permissionRequired = permissionRequired;
        return this;
    }

    public ValidityStatus isValid(){
        if(spawnPoint == null)
            return ValidityStatus.INVALID_SPAWN_LOCATION;

        if(endPoint == null)
            return ValidityStatus.INVALID_END_LOCATION;

        if(pos1 == null)
            return ValidityStatus.INVALID_POSITION_1;

        if(pos2 == null)
            return ValidityStatus.INVALID_POSITION_2;

        return ValidityStatus.VALID;
    }

    public Island asIsland(){
        if(minBlocksRequired <= 0)
            minBlocksRequired = -1;

        if(minSecRequired <= 0)
            minSecRequired = -1;

        return new Island(islandName, permissionRequired, spawnPoint, endPoint, pos1, pos2, minBlocksRequired, minSecRequired);
    }

    public static SetupSession asSession(@NotNull Player player, @NotNull Island island){
        return new SetupSession(player.getUniqueId(), island.getIslandName())
                .setPos1(island.getPos1())
                .setPos2(island.getPos2())
                .setSpawnPoint(island.getSpawnLocation())
                .setEndPoint(island.getEndLocation())
                .setMinBlocksRequired(island.getMinBlocksRequired())
                .setMinSecRequired(island.getMinTimeRequired())
                .setPermissionRequired(island.getPermission());
    }

    public Map<String, Object> asSerializedSession(){
        final Map<String, Object> map = new HashMap<>();
        map.put("name", islandName);
        map.put("enabled", false);
        map.put("min-req", new HashMap<String, Object>()
                {{
                    put("blocks", minBlocksRequired);
                    put("sec", minSecRequired);
                }}
        );
        map.put("perm", permissionRequired == null ? "" : permissionRequired);
        map.put("spawn", FlatFileUtils.serializeLocation(spawnPoint));
        map.put("end", FlatFileUtils.serializeLocation(endPoint));
        map.put("pos1", FlatFileUtils.serializeLocation(pos1));
        map.put("pos2", FlatFileUtils.serializeLocation(pos2));
        return map;
    }

    public enum ValidityStatus{
        VALID,
        INVALID_SPAWN_LOCATION,
        INVALID_END_LOCATION,
        INVALID_POSITION_1,
        INVALID_POSITION_2,
    }
}

package io.github.alenalex.bridger.variables;

public enum ConfigurationPaths {
    STORAGE_TYPE("storage.type"),
    STORAGE_HOST("host"),
    STORAGE_PORT("port"),
    STORAGE_DATABASE_NAME("database"),
    STORAGE_USERNAME("username"),
    STORAGE_PASSWORD("password"),
    STORAGE_SSL("ssl"),

    ACTION_BAR_UPDATE_RATE("update-action-bar-on-every-ms"),

    SPAWN_LOCATION("spawn-location"),

    COSMETICS_FIRE_WORK_ENABLED("cosmetics.firework.enabled"),
    COSMETICS_ALLOWED_FIREWORK_MODELS("cosmetics.firework.allowed-firework-models"),

    COSMETICS_DEFAULT_MATERIAL("cosmetics.material.default"),
    COSMETICS_MATERIALS_ENABLED("cosmetics.material.allowed-materials"),

    BROADCAST_NEW_RECORD("broadcast-new-best-time-all-players"),

    ALLOW_BREAKING_BLOCK_ON_LOBBY("allow-block-interaction-on-lobby.place-block"),
    ALLOW_PLACING_BLOCK_ON_LOBBY("allow-block-interaction-on-lobby.break-block"),

    COMMAND_TO_BLOCK("blocked-commands"),

    VOID_DETECTION_HEIGHT("void-detection.detection-height"),
    DETECT_VOID_FALL_ON_LOBBY("void-detection.detect-on.lobby"),
    DETECT_VOID_FALL_WHILE_SPECTATOR("void-detection.detect-on.spectating"),

    PLACEMENT_BLOCKED_MATERIALS("deny-block-placement-on"),

    CHEAT_PROTECTION_MIN_TIME("cheat-protection.kick-if-failed-min-time"),
    CHEAT_PROTECTION_MIN_BLOCK("cheat-protection.kick-if-failed-min-blocks-req"),
    CHEAT_PROTECTION_REACHED_IN_IDLE("cheat-protection.kick-if-reached-to-end-without-starting"),

    LOBBY_SETTINGS("lobby-items.settings"),
    LOBBY_SHOP("lobby-items.shop"),
    LOBBY_SELECTOR("lobby-items.island-selector"),
    LOBBY_JOIN("lobby-items.join"),
    LOBBY_OTHERS("lobby-items.others"),

    SERVER_JOIN_MESSAGE("server-join-message"),
    SERVER_LEAVE_MESSAGE("server-leave-message"),
    ;

    private String path;

    ConfigurationPaths(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}

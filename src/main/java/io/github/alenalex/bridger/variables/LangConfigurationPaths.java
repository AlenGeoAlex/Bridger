package io.github.alenalex.bridger.variables;

public enum LangConfigurationPaths {
    KICK_MESSAGE_FAILED_TO_LOAD_DATA("kick-message.failed-to-load-data"),
    NO_FREE_ISLANDS("no-free-islands"),
    TELEPORTED_TO_ISLAND("teleporting-to-island"),
    PLAYER_QUIT_MATCH("player-quit-game"),
    UNABLE_TO_PROCESS_SELECTED_FIREWORK("unable-to-process-selected-firework"),
    KICK_ISLAND_DISABLED("kicked-island-disable"),
    PLAYER_LEAVE_GAME("player-leave-game"),

    SHOP_SUCCESSFULLY_PURCHASED("shop-purchase.bought-successfully"),
    SHOP_PURCHASE_FAIL_NO_CASH("shop-purchase.not-enough-cash"),

    NEW_BEST_TIME_TO_PLAYER("new-best-time.to-player"),
    NEW_BEST_TIME_TO_BROADCAST("new-best-time.as-broadcast"),

    PLAYER_STARTED_BRIDGING("player-started-bridging"),
    CANNOT_PLACE_BLOCKS_WHILE_SPECTATING("cannot-place-blocks-while-spectating"),
    BLOCKED_COMMAND("command-blocked"),

    CANNOT_SPECTATE_WHILE_IN_GAME("cannot-spectate-while-in-game"),
    NO_ISLAND_FOUND("no-island-found"),
    SPECTATING_ON("spectating-on"),
    CANNOT_SPECTATE_NON_PLAYERS("cannot-spectating-not-players"),

    ISLAND_OCCUPIED_GUI("the-island-is-occupied-gui"),
    ACTIVITY_BLOCKED("activity_blocked"),
    ISLAND_STILL_RESETTING_BLOCKS_PLACED("island-is-resetting-your-blocks"),

    NOT_PROVIDED_VALID_ISLAND_NAME("command.did-not-provide-an-island-name"),
    TELEPORTED_VOID_DETECTION("teleported-back-void-detection"),
    CANNOT_PLACE_BLOCK_HERE("cannot-place-block-here"),

    REACHED_TO_END_ON_IDLE("reached-end-without-placing-blocks"),

    CHEAT_PROTECTION_MIN_TIME("cheat-protection.failed-min-time"),
    CHEAT_PROTECTION_MIN_BLOCK("cheat-protection."),
    CHEAT_PROTECTION_REACHED_IN_IDLE("cheat-protection."),
    ;
    private final String path;

    LangConfigurationPaths(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}

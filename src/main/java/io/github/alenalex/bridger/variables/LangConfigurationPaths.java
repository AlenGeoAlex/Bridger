package io.github.alenalex.bridger.variables;

public enum LangConfigurationPaths {
    KICK_MESSAGE_FAILED_TO_LOAD_DATA("kick-message.failed-to-load-data"),
    NO_FREE_ISLANDS("no-free-islands"),
    TELEPORTED_TO_ISLAND("teleporting-to-island"),
    PLAYER_QUIT_MATCH("player-quit-game"),
    UNABLE_TO_PROCESS_SELECTED_FIREWORK("unable-to-process-selected-firework"),

    SHOP_SUCCESSFULLY_PURCHASED("shop-purchase.bought-successfully"),
    SHOP_PURCHASE_FAIL_NO_CASH("shop-purchase.not-enough-cash"),
    ;
    private final String path;

    LangConfigurationPaths(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}

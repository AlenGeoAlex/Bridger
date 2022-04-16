package io.github.alenalex.bridger.variables;

public enum LangConfigurationPaths {
    KICK_MESSAGE_FAILED_TO_LOAD_DATA("kick-message.failed-to-load-data"),
    NO_FREE_ISLANDS("no-free-islands"),
    TELEPORTED_TO_ISLAND("teleporting-to-island"),
    PLAYER_QUIT_MATCH("player-quit-game"),
    UNABLE_TO_PROCESS_SELECTED_FIREWORK("unable-to-process-selected-firework"),
    ;
    private final String path;

    LangConfigurationPaths(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}

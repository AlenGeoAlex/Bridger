package io.github.alenalex.bridger.variables;

public enum LangConfigurationPaths {
    PREFIX("prefix"),
    KICK_MESSAGE_FAILED_TO_LOAD_DATA("kick-message.failed-to-load-data"),
    ;
    private final String path;

    LangConfigurationPaths(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}

package io.github.alenalex.bridger.variables;

public enum ConfigurationPaths {
    STORAGE_TYPE("storage.type"),
    STORAGE_HOST("host"),
    STORAGE_PORT("port"),
    STORAGE_DATABASE_NAME("database"),
    STORAGE_USERNAME("username"),
    STORAGE_PASSWORD("password"),
    STORAGE_SSL("ssl"),

    SPAWN_LOCATION("spawn-location"),
    ;

    private String path;

    ConfigurationPaths(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}

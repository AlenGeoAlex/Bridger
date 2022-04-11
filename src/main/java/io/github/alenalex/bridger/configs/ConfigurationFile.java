package io.github.alenalex.bridger.configs;

import io.github.alenalex.bridger.abstracts.AbstractSettings;
import io.github.alenalex.bridger.handler.ConfigurationHandler;
import io.github.alenalex.bridger.variables.ConfigurationPaths;

public class ConfigurationFile extends AbstractSettings {

    private String storageType;

    public ConfigurationFile(ConfigurationHandler handler) {
        super(handler);
    }

    @Override
    public void loadFile() {
        this.storageType = this.file.getString(ConfigurationPaths.STORAGE_TYPE.getPath());

    }

    @Override
    public void prepareReload() {

    }

    public String getStorageType() {
        return storageType;
    }
}

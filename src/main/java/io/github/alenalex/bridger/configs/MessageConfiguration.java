package io.github.alenalex.bridger.configs;

import io.github.alenalex.bridger.abstracts.AbstractSettings;
import io.github.alenalex.bridger.handler.ConfigurationHandler;
import io.github.alenalex.bridger.variables.LangConfigurationPaths;

public class MessageConfiguration extends AbstractSettings {

    private String pluginPrefix;

    public MessageConfiguration(ConfigurationHandler handler) {
        super(handler);
    }

    @Override
    public void loadFile() {
        this.pluginPrefix = this.file.getString(LangConfigurationPaths.PREFIX.getPath());
    }

    @Override
    public void prepareReload() {

    }

    public String getPluginPrefix() {
        return pluginPrefix;
    }
}

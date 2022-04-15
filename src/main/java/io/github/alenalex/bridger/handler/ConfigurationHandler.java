package io.github.alenalex.bridger.handler;

import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.configs.ConfigurationFile;
import io.github.alenalex.bridger.interfaces.IHandler;

import java.io.File;

public class ConfigurationHandler implements IHandler {

    private final Bridger plugin;
    private final ConfigurationFile configurationFile;

    public ConfigurationHandler(Bridger plugin) {
        this.plugin = plugin;
        this.configurationFile = new ConfigurationFile(this);
    }

    @Override
    public boolean initHandler() {
        return configurationFile.initConfigFile("config.yml", plugin.getDataFolder().getPath(), plugin().getResource("config.yml"))
                ;

    }

    @Override
    public void prepareHandler() {
        configurationFile.loadFile();
    }

    @Override
    public Bridger plugin(){
        return plugin;
    }

    @Override
    public void reloadHandler() {
        configurationFile.prepareReload();
    }

    public ConfigurationFile getConfigurationFile() {
        return configurationFile;
    }

}

package io.github.alenalex.bridger.handler;

import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.configs.ConfigurationFile;
import io.github.alenalex.bridger.configs.MessageConfiguration;
import io.github.alenalex.bridger.interfaces.IHandler;

import java.io.File;

public class ConfigurationHandler implements IHandler {

    private final Bridger plugin;
    private final ConfigurationFile configurationFile;
    private final MessageConfiguration messageConfiguration;

    public ConfigurationHandler(Bridger plugin) {
        this.plugin = plugin;
        this.configurationFile = new ConfigurationFile(this);
        this.messageConfiguration = new MessageConfiguration(this);
    }

    @Override
    public boolean initHandler() {
        return configurationFile.initConfigFile("config.yml", plugin.getDataFolder().getPath(), plugin().getResource("config.yml"))
                && messageConfiguration.initConfigFile("lang.yml", plugin.getDataFolder().getPath()+ File.separator+"lang", plugin().getResource("lang.yml"))
                ;

    }

    @Override
    public void prepareHandler() {
        configurationFile.loadFile();
        messageConfiguration.loadFile();
    }

    @Override
    public Bridger plugin(){
        return plugin;
    }

    @Override
    public void reloadHandler() {
        configurationFile.prepareReload();
        messageConfiguration.prepareReload();
    }

    public ConfigurationFile getConfigurationFile() {
        return configurationFile;
    }

    public MessageConfiguration getMessageConfiguration() {
        return messageConfiguration;
    }
}

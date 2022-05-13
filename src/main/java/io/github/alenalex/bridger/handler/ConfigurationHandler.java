package io.github.alenalex.bridger.handler;

import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.configs.*;
import io.github.alenalex.bridger.interfaces.IHandler;

import java.io.File;

public final class ConfigurationHandler implements IHandler {

    private final Bridger plugin;
    private final ConfigurationFile configurationFile;
    private final UIConfiguration uiConfiguration;
    private final IslandConfiguration islandConfiguration;
    private final ScoreboardConfiguration scoreboardConfiguration;
    private final HologramConfig hologramConfig;

    public ConfigurationHandler(Bridger plugin) {
        this.plugin = plugin;
        this.configurationFile = new ConfigurationFile(this);
        this.uiConfiguration = new UIConfiguration(this);
        this.islandConfiguration = new IslandConfiguration(this);
        this.scoreboardConfiguration = new ScoreboardConfiguration(this);
        this.hologramConfig = new HologramConfig(this);
    }

    @Override
    public boolean initHandler() {
        if(!plugin.getDataFolder().exists())
            plugin.getDataFolder().mkdirs();

        return configurationFile.initConfigFile("config.yml", plugin.getDataFolder().getPath(), plugin().getResource("config.yml"))
                && uiConfiguration.initConfigFile("gui.yml", plugin.getDataFolder().getPath()+File.separator+"gui", plugin().getResource("gui/gui.yml"))
                && islandConfiguration.initYamlFile("islands.yml", plugin.getDataFolder().getPath()+File.separator+"data")
                && scoreboardConfiguration.initYamlFile("scoreboard.yml", plugin.getDataFolder().getPath(), plugin.getResource("scoreboard.yml"))
                && hologramConfig.initYamlFile("hologram.yml", plugin.getDataFolder().getPath(), plugin.getResource("hologram.yml"));


    }

    @Override
    public void prepareHandler() {
        configurationFile.loadFile();
        uiConfiguration.loadFile();
        islandConfiguration.loadFile();
        scoreboardConfiguration.loadFile();
        hologramConfig.loadFile();
    }

    @Override
    public Bridger plugin(){
        return plugin;
    }

    @Override
    public void reloadHandler() {
        configurationFile.prepareReload();
        uiConfiguration.prepareReload();
        islandConfiguration.prepareReload();
        scoreboardConfiguration.prepareReload();
        hologramConfig.prepareReload();
        this.prepareHandler();
    }


    public ConfigurationFile getConfigurationFile() {
        return configurationFile;
    }

    public UIConfiguration getUiConfiguration() {
        return uiConfiguration;
    }

    public IslandConfiguration getIslandConfiguration() {
        return islandConfiguration;
    }

    public ScoreboardConfiguration getScoreboardConfiguration() {
        return scoreboardConfiguration;
    }

    public HologramConfig getHologramConfig() {
        return hologramConfig;
    }
}

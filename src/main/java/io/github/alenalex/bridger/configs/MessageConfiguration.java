package io.github.alenalex.bridger.configs;


import de.leonhard.storage.Yaml;
import de.leonhard.storage.internal.FlatFile;
import de.leonhard.storage.internal.settings.ReloadSettings;
import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.utils.adventure.MessageFormatter;
import io.github.alenalex.bridger.variables.LangConfigurationPaths;
import net.kyori.adventure.text.Component;

import java.io.File;
import java.util.List;

public class MessageConfiguration {

    private final Bridger plugin;
    private final FlatFile file;

    public MessageConfiguration(Bridger plugin, FlatFile file) {
        this.plugin = plugin;
        this.file = file;
        this.file.setReloadSettings(ReloadSettings.AUTOMATICALLY);
    }

    public MessageConfiguration(Bridger plugin, File file) {
        this.plugin = plugin;
        this.file = new Yaml(file);
        this.file.setReloadSettings(ReloadSettings.AUTOMATICALLY);
    }

    public FlatFile getFile() {
        return file;
    }

    public String asString(String path) {
        return file.getString(path);
    }

    public Component asComponent(String path){
        return MessageFormatter.transform(asString(path));
    }

    public String asString(LangConfigurationPaths path) {
        return asString(path.getPath());
    }

    public Component asComponent(LangConfigurationPaths path){
        return asComponent(path.getPath());
    }

    public List<String> asStringList(String path) {
        return file.getStringList(path);
    }

    public List<Component> asComponentList(String path){
        return MessageFormatter.transform(asStringList(path));
    }

}

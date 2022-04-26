package io.github.alenalex.bridger.configs;


import de.leonhard.storage.Yaml;
import de.leonhard.storage.internal.FlatFile;
import de.leonhard.storage.internal.settings.ReloadSettings;
import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.utils.adventure.MessageFormatter;
import io.github.alenalex.bridger.utils.adventure.internal.MessagePlaceholder;
import io.github.alenalex.bridger.variables.LangConfigurationPaths;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

public class MessageConfiguration {

    private @NotNull final Bridger plugin;
    private @NotNull final FlatFile file;

    public MessageConfiguration(Bridger plugin, @NotNull FlatFile file) {
        this.plugin = plugin;
        this.file = file;
        this.file.setReloadSettings(ReloadSettings.AUTOMATICALLY);
    }

    public MessageConfiguration(Bridger plugin, @NotNull File file) {
        this.plugin = plugin;
        this.file = new Yaml(file);
        this.file.setReloadSettings(ReloadSettings.AUTOMATICALLY);
    }

    @NotNull
    public FlatFile getFile() {
        return file;
    }

    public String asString(String path) {
        if(!this.file.contains(path))
            return "<red>This language node ["+path+"] doesn't exist! Please contact an administrator";

        return file.getString(path);
    }

    public Component asComponent(String path){
        return MessageFormatter.transform(asString(path));
    }

    public Component asComponent(String path, MessagePlaceholder... placeholders){
        return MessageFormatter.transform(asString(path), placeholders);
    }

    public Component asComponent(LangConfigurationPaths paths, MessagePlaceholder... placeholders){
        return MessageFormatter.transform(asString(paths.getPath()), placeholders);
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

    public String asLegacyColorizedString(String path){
        return MessageFormatter.colorizeLegacy(asString(path));
    }

    public List<String> asLegacyColorizedStringList(String path){
        return MessageFormatter.colorizeLegacy(asStringList(path));
    }

}

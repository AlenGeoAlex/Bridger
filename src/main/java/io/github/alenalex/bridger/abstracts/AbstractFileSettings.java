package io.github.alenalex.bridger.abstracts;

import de.leonhard.storage.Config;
import de.leonhard.storage.Json;
import de.leonhard.storage.Yaml;
import de.leonhard.storage.internal.FlatFile;
import de.leonhard.storage.sections.FlatFileSection;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import io.github.alenalex.bridger.exceptions.IllegalFlatFileException;
import io.github.alenalex.bridger.handler.ConfigurationHandler;
import io.github.alenalex.bridger.utils.FlatFileUtils;
import io.github.alenalex.bridger.utils.HeadUtils;
import io.github.alenalex.bridger.utils.adventure.MessageFormatter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractFileSettings {

    public static final ItemStack DEFAULT_ITEM_STACK = new ItemStack(Material.AIR);

    protected final ConfigurationHandler handler;
    protected FlatFile file;

    public AbstractFileSettings(ConfigurationHandler handler) {
        this.handler = handler;
    }

    public boolean initConfigFile(@NotNull String name, @NotNull String path){
        try {
            this.file = new Config(name, path);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        doDefaults();
        return true;
    }

    public boolean initConfigFile(@NotNull String name, @NotNull String path, @NotNull InputStream inputStream){
        try {
            this.file = new Config(name, path, inputStream);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        doDefaults();
        return true;
    }

    public boolean initYamlFile(@NotNull String name, @NotNull String path){
        try {
            this.file = new Yaml(name, path);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean initYamlFile(File file){
        try {
            this.file = new Yaml(file);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean initYamlFile(@NotNull String name, @NotNull String path, @NotNull InputStream inputStream){
        try {
            this.file = new Yaml(name, path, inputStream);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean initJsonFile(@NotNull String name, @NotNull String path){
        try {
            this.file = new Json(name, path);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean initJsonFile(@NotNull String name, @NotNull String path, @NotNull InputStream inputStream){
        try {
            this.file = new Json(name, path, inputStream);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }


    protected void doDefaults(){
        //Like add some-settings and all
    }

    public FlatFileSection getSectionOf(@NotNull String path){
        if(file == null)
            throw new IllegalFlatFileException("The file is not yet initialized!");

        return FlatFileUtils.getSectionOf(this.file, path);
    }

    /**
     * Deserialize a location from the string of a given path.
     * Serialization is based on {@link FlatFileUtils#serializeLocation(Location)}
     * @param path path to the string
     * @return
     */
    public Optional<Location> deserializeLocation(@NotNull String path){
        return FlatFileUtils.deserializeLocation(getSectionOf(path));
    }

    public Map<String, Object> serializeLocation(@NotNull Location location){
        return FlatFileUtils.serializeLocation(location);
    }

    public Optional<ItemStack> deserializeItemStack(@NotNull String path){
        String itemStackString = file.getString(path);
        return Optional.ofNullable(FlatFileUtils.deserializeItemStack(itemStackString));
    }

    public FlatFile file(){
        return file;
    }

    public Component getComponent(@NotNull String path){
        return MessageFormatter.transform(file.getString(path));
    }

    public List<Component> getComponentList(@NotNull String path){
        return MessageFormatter.transform(file.getStringList(path));
    }

    public abstract void loadFile();

    public abstract void prepareReload();

    public void flushData(){
        this.file.getFileData().clear();
    }
}

package io.github.alenalex.bridger.configs;

import de.leonhard.storage.internal.FlatFile;
import io.github.alenalex.bridger.abstracts.AbstractFileSettings;
import io.github.alenalex.bridger.handler.ConfigurationHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class IslandConfiguration extends AbstractFileSettings {

    public IslandConfiguration(ConfigurationHandler handler) {
        super(handler);
    }

    @Override
    public void loadFile() {

    }

    @Override
    public void prepareReload() {

    }

    public void setIslandData(@NotNull String islandName , @NotNull Map<String, Object> data){
        file.set(islandName, data);
    }

    public boolean containsIslandData(@NotNull String islandName){
        return file.contains(islandName);
    }

    public FlatFile file(){
        return file;
    }


}

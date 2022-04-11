package io.github.alenalex.bridger.utils;

import de.leonhard.storage.internal.FlatFile;
import de.leonhard.storage.sections.FlatFileSection;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class FlatFileUtils {

    public static FlatFileSection getSectionOf(@NotNull FlatFile file, @NotNull String path){
        return new FlatFileSection(file, path);
    }

    public static Map<String, Object> serializeLocation(@NotNull Location location){
        final Map<String, Object> serializedMap = new HashMap<>();
        serializedMap.put("world-name", location.getWorld().getName());
        serializedMap.put("x", location.getBlockX());
        serializedMap.put("y", location.getBlockY());
        serializedMap.put("z", location.getBlockZ());
        serializedMap.put("yaw", location.getYaw());
        serializedMap.put("pitch", location.getPitch());
        return serializedMap;
    }


}

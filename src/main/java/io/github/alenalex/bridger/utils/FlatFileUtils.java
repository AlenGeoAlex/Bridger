package io.github.alenalex.bridger.utils;

import de.leonhard.storage.internal.FlatFile;
import de.leonhard.storage.sections.FlatFileSection;
import io.github.alenalex.bridger.Bridger;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
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

    public static ItemStack deserializeItemStack(@NotNull FlatFileSection section , @NotNull String path){
        String itemStackString = section.getString(path);

        if(StringUtils.isEmpty(itemStackString))
            return null;

        if(itemStackString.startsWith("[BASE64]:")){
            final String[] split = itemStackString.split(":");
            if(split.length != 2)
                return null;

            final String base64 = split[1];
            return HeadUtils.getHead(base64);
        }else {
            return EnumUtils.isValidEnum(Material.class, itemStackString) ? new ItemStack(Material.valueOf(itemStackString)) : null;
        }
    }

    public static ItemStack deserializeItemStack(@NotNull String itemStackString){

        if(StringUtils.isEmpty(itemStackString))
            return null;

        if(itemStackString.startsWith("[BASE64]:")){
            final String[] split = itemStackString.split(":");
            if(split.length != 2)
                return null;

            final String base64 = split[1];
            return HeadUtils.getHead(base64);
        }else {
            return EnumUtils.isValidEnum(Material.class, itemStackString) ? new ItemStack(Material.valueOf(itemStackString)) : null;
        }
    }
}

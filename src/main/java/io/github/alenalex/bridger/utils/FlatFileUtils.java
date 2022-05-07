package io.github.alenalex.bridger.utils;

import de.leonhard.storage.internal.FlatFile;
import de.leonhard.storage.sections.FlatFileSection;
import io.github.alenalex.bridger.Bridger;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

    @Nullable
    public static ItemStack deserializeItemStack(@NotNull String itemStackString){

        if(StringUtils.isEmpty(itemStackString))
            return null;

        if(itemStackString.startsWith("[BASE64]:")) {
            final String[] split = itemStackString.split(":");
            if (split.length != 2) {
                Bridger.instance().getLogger().warning("An illegal configuration entry has been found in "+itemStackString+". provide like [BASE64]:value");
                return null;
            }

            final String base64 = split[1];
            return HeadUtils.getHead(base64);
        }else if(itemStackString.startsWith("[LEGACY]:")){
            final String[] split = itemStackString.split(":");

            if (split.length != 3) {
                Bridger.instance().getLogger().warning("An illegal configuration entry has been found in "+itemStackString+". provide like [LEGACY]:MATERIAL_NAME:DATA");
                return null;
            }

            if(!EnumUtils.isValidEnum(Material.class, split[1])){
                return null;
            }

            final Material material = Material.getMaterial(split[1]);
            final byte data = Byte.parseByte(split[2]);

            return new ItemStack(material, 1, data);
        }else {
            return EnumUtils.isValidEnum(Material.class, itemStackString) ? new ItemStack(Material.valueOf(itemStackString)) : null;
        }
    }

    public static Optional<Location> deserializeLocation(@NotNull FlatFileSection section){
        final String worldName = section.getString("world-name");
        if(StringUtils.isBlank(worldName))
            return Optional.empty();
        final World world = Bukkit.getWorld(worldName);

        if(!section.contains("x") && !section.contains("y") || !section.contains("z"))
            return Optional.empty();

        final int blockX = section.getInt("x");
        final int blockY = section.getInt("y");
        final int blockZ = section.getInt("z");

        final float yaw = section.getFloat("yaw");
        final float pitch = section.getFloat("pitch");

        return Optional.of(new Location(world, blockX, blockY, blockZ, yaw, pitch));
    }
}

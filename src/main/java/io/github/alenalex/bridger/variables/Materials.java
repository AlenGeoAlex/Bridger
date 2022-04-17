package io.github.alenalex.bridger.variables;

import io.github.alenalex.bridger.Bridger;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class Materials {

    public static ItemStack getItemStackByMaterialName(@NotNull String materialName){
        if(StringUtils.isBlank(materialName))
            return Bridger
                    .instance()
                    .configurationHandler()
                    .getConfigurationFile()
                    .getDefaultMaterial();

        final ItemStack stack = deserializeItemStack(materialName);
        if(stack != null && isMaterialEnabled(stack))
            return stack;

        else return Bridger
                .instance()
                .configurationHandler()
                .getConfigurationFile()
                .getDefaultMaterial();
    }

    public static String serializeItemStack(@NotNull ItemStack stack){
        final Material material = stack.getType();
        switch (material){
            case STAINED_GLASS:
            case STAINED_GLASS_PANE:
            case WOOL:
                return material.name()+":"+stack.getData().getData();
            default:
                return material.name();
        }
    }

    @Nullable
    public static ItemStack deserializeItemStack(@NotNull String materialName){
        Material material = validateAndGetMaterial(materialName);
        if(material != null)
            return new ItemStack(material);

        if(materialName.contains(":")){
            String[] args = materialName.split(":");
            if(args.length == 2){
                final Material dataMaterial = validateAndGetMaterial(args[0]);
                if(dataMaterial == null)
                    return null;

                return new ItemStack(dataMaterial, 1, Byte.parseByte(args[1]));
            }else return null;
        }else return null;
    }

    @Nullable
    public static Material validateAndGetMaterial(@NotNull String materialName){
        if(EnumUtils.isValidEnum(Material.class, materialName)){
            return Material.getMaterial(materialName);
        }else return null;
    }

    /*
    //This is a limitation, can't really check colored wool etc....
    public static boolean isMaterialEnabled(@NotNull ItemStack stack){
        return Bridger
                .instance()
                .configurationHandler()
                .getConfigurationFile()
                .getEnabledMaterials()
                .keySet()
                .stream()
                .anyMatch(material -> material.getType() == stack.getType());
    }
     */

    public static boolean isMaterialEnabled(@NotNull ItemStack stack){
        return Bridger
                .instance()
                .configurationHandler()
                .getConfigurationFile()
                .getEnabledMaterials()
                .keySet()
                .stream()
                .anyMatch(material -> material.isSimilar(stack));
    }

    public static boolean isMaterialTypeEnabled(@NotNull Material material){
        return Bridger
                .instance()
                .configurationHandler()
                .getConfigurationFile()
                .getEnabledMaterials()
                .keySet()
                .stream()
                .anyMatch(stack -> material == stack.getType());
    }

}

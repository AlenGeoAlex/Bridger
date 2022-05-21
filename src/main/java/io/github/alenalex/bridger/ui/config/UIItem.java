package io.github.alenalex.bridger.ui.config;

import de.leonhard.storage.sections.FlatFileSection;
import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.exceptions.IllegalUIAccess;
import io.github.alenalex.bridger.utils.FlatFileUtils;
import io.github.alenalex.bridger.utils.adventure.MessageFormatter;
import io.github.alenalex.bridger.utils.adventure.internal.MessagePlaceholder;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class UIItem {

    private static final Bridger plugin;

    static {
        plugin = Bridger.instance();
    }

    private final String name;
    private final int slot;
    private final List<String> lore;
    private final int amount;
    private final ItemStack itemStack;
    private boolean glow;

    public UIItem(String name, int slot, List<String> lore, int amount, ItemStack itemStack) {
        int amount1;
        this.name = name;
        this.slot = slot;
        this.lore = lore;
        amount1 = amount;
        this.itemStack = itemStack;

        if(amount <= 0) {
            amount1 = 1;
        }else amount1 = amount;
        this.amount = amount1;
    }

    public UIItem(String name, int slot, List<String> lore, int amount, ItemStack itemStack, boolean glow) {
        int amount1;
        this.name = name;
        this.slot = slot;
        this.lore = lore;
        amount1 = amount;
        this.itemStack = itemStack;

        if(amount <= 0) {
            amount1 = 1;
        }else amount1 = amount;
        this.amount = amount1;
        this.glow = glow;
    }



    public String nameAsString(MessagePlaceholder... placeholders){
        return MessagePlaceholder.replacePlaceholders(Arrays.asList(placeholders), name);
    }

    public Component nameAsComponent(MessagePlaceholder... placeholders){
        return MessageFormatter.transform(MessagePlaceholder.replacePlaceholders(Arrays.asList(placeholders), name));
    }

    public int slot() {
        return slot;
    }

    public int amount() {
        return amount;
    }

    public ItemStack itemStack(){
        return itemStack;
    }

    public List<String> loreAsString(MessagePlaceholder... placeholders){
        return MessagePlaceholder.replacePlaceholders(Arrays.asList(placeholders), lore);
    }

    public List<Component> loreAsComponent(MessagePlaceholder... placeholders){
        return MessageFormatter.transform(MessagePlaceholder.replacePlaceholders(Arrays.asList(placeholders), lore));
    }

    public boolean shouldGlow(){
        return glow;
    }

    public List<String> lore(){
        return lore;
    }

    public static UIItem buildFrom(@NotNull FlatFileSection section){
        return buildFrom(section, false);
    }

    @Nullable
    public static UIItem buildAsNullable(@NotNull FlatFileSection section){
        ItemStack stack = FlatFileUtils.deserializeItemStack(section.getString("item"));
        final String name = section.getString("name");
        final List<String> loreList = section.getStringList("lore");
        final int slot = section.getInt("slot");
        final int amount = section.getInt("amount");
        final boolean glow = section.contains("glow") && section.getBoolean("glow");

        if(stack == null)
            return null;

        return new UIItem(name, slot, loreList, amount, stack);
    }

    public static UIItem buildFrom(@NotNull FlatFileSection section, boolean allowDummyMaterial){
        ItemStack stack = FlatFileUtils.deserializeItemStack(section.getString("item"));
        final String name = section.getString("name");
        final List<String> loreList = section.getStringList("lore");
        final int slot = section.getInt("slot");
        final int amount = section.getInt("amount");
        final boolean glow = section.contains("glow") && section.getBoolean("glow");

        if(stack == null) {
            if(allowDummyMaterial)
                stack = new ItemStack(Material.STONE);
            else
                throw new IllegalUIAccess("The item in the config " + section.getPathPrefix() + " is null");
        }
        return new UIItem(name, slot, loreList, amount, stack);
    }

}

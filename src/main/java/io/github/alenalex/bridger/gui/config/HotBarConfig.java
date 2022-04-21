package io.github.alenalex.bridger.gui.config;

import de.leonhard.storage.sections.FlatFileSection;
import io.github.alenalex.bridger.utils.FlatFileUtils;
import io.github.alenalex.bridger.utils.adventure.MessageFormatter;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HotBarConfig {

    private final ItemStack itemStack;
    private final int slot;
    private final Component name;
    private final List<Component> componentList;
    private final List<String> commands;

    public HotBarConfig(ItemStack itemStack, int slot, Component name, List<Component> componentList, List<String> commands) {
        this.itemStack = itemStack;
        this.slot = slot;
        this.name = name;
        this.componentList = componentList;
        this.commands = commands;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public int getSlot() {
        return slot;
    }

    public Component getName() {
        return name;
    }

    public List<Component> getComponentList() {
        return componentList;
    }

    public List<String> getCommands() {
        return commands;
    }

    public boolean hasCommands() {
        return !commands.isEmpty();
    }

    public static HotBarConfig of(@NotNull FlatFileSection section){
        if(section.contains("enabled") && !section.getBoolean("enabled")){
            return null;
        }

        final ItemStack stack = FlatFileUtils.deserializeItemStack(section.getString("item"));
        final Component text = MessageFormatter.transform(section.getString("name"));
        final List<Component> components = new ArrayList<>();
        for(String s : section.getStringList("lore")){
            components.add(MessageFormatter.transform(s));
        }
        final int slot = section.getInt("slot");

        if(stack == null)
            throw new IllegalArgumentException("Invalid item stack");

        return new HotBarConfig(stack, slot, text, components, section.getStringList("commands"));
    }

    public void applyOn(@NotNull Player player){
        player.getInventory().setItem(slot, itemStack);
        player.updateInventory();
    }
}
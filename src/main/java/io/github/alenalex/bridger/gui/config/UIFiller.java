package io.github.alenalex.bridger.gui.config;

import io.github.alenalex.bridger.Bridger;

import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class UIFiller {

    private static final Bridger plugin;

    static {
        plugin = Bridger.instance();
    }

    private ItemStack itemStack;
    private List<Integer> slots;

    public UIFiller(ItemStack itemStack, List<Integer> slots) {
        this.itemStack = itemStack;

        this.slots = slots;
    }

    public ItemStack itemStack() {
        return itemStack;
    }



    public List<Integer> slots() {
        return slots;
    }

    public UIFiller setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
        return this;
    }



    public UIFiller setSlots(List<Integer> slots) {
        this.slots = slots;
        return this;
    }

}

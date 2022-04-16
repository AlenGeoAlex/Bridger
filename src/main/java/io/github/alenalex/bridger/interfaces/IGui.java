package io.github.alenalex.bridger.interfaces;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.BaseGui;
import dev.triumphteam.gui.guis.GuiItem;
import io.github.alenalex.bridger.configs.UIConfiguration;
import io.github.alenalex.bridger.exceptions.IllegalUIAccess;
import io.github.alenalex.bridger.gui.config.UIConfig;
import io.github.alenalex.bridger.gui.config.UIFiller;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface IGui {

    UIConfiguration getConfiguration();

    void openFor(@NotNull Player player, Object... params);

    default void openFor(@NotNull Player player) {
        openFor(player, null);
    }

    default <T extends BaseGui>  T applyFiller(T gui, @NotNull UIConfig config) {
        return applyFiller(gui, config.getFillers());
    }

    default <T extends BaseGui>  T applyFiller(T gui, @NotNull List<UIFiller> fillers) {
        if(gui == null)
            throw new IllegalUIAccess("The gui provided for the filler is null");

        if(fillers.isEmpty())
            return gui;

        for(UIFiller filler : fillers){
            final GuiItem item = ItemBuilder.from(filler.itemStack())
                    .name(Component.empty())
                    .lore(Component.empty())
                    .asGuiItem();

            for(Integer slots : filler.slots()){
                gui.setItem(slots, item);
            }
        }

        return gui;
    }

    default <T extends BaseGui>  T applyFiller(T gui, @NotNull UIFiller filler) {
        if(gui == null)
            throw new IllegalUIAccess("The gui provided for the filler is null");

        final GuiItem item = ItemBuilder.from(filler.itemStack())
                .name(Component.empty())
                .lore(Component.empty())
                .asGuiItem();

        for(Integer slots : filler.slots()){
            gui.setItem(slots, item);
        }


        return gui;
    }

}

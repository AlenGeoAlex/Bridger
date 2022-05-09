package io.github.alenalex.bridger.ui.dynamic.profile;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import io.github.alenalex.bridger.abstracts.AbstractDynamicGUI;
import io.github.alenalex.bridger.handler.UIHandler;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public final class PlayerSettings extends AbstractDynamicGUI<Gui> {

    public PlayerSettings(UIHandler handler) {
        super(handler);
    }

    @Override
    public CompletableFuture<Gui> prepGui(@NotNull Player player, Object... params) {
        return CompletableFuture.supplyAsync(new Supplier<Gui>() {
            @Override
            public Gui get() {
                final Gui gui = Gui
                        .gui()
                        .disableAllInteractions()
                        .create();

                applyFiller(gui, getConfiguration().getPlayerSettingConfig());

                final GuiItem closeItem = ItemBuilder
                        .from(getConfiguration().getPlayerSelectingClose().itemStack())
                        .name(getConfiguration().getPlayerSelectingClose().nameAsComponent())
                        .lore(getConfiguration().getPlayerSelectingClose().loreAsComponent())
                        .asGuiItem(event -> gui.close(player));

                gui.setItem(getConfiguration().getPlayerSelectingClose().slot(), closeItem);

                final GuiItem fireworkButton = ItemBuilder
                        .from(getConfiguration().getPlayerSelectingFirework().itemStack())
                        .name(getConfiguration().getPlayerSelectingFirework().nameAsComponent())
                        .lore(getConfiguration().getPlayerSelectingFirework().loreAsComponent())
                        .asGuiItem();

                gui.setItem(getConfiguration().getPlayerSelectingFirework().slot(), fireworkButton);

                final GuiItem particleButton = ItemBuilder
                        .from(getConfiguration().getPlayerSelectingParticle().itemStack())
                        .name(getConfiguration().getPlayerSelectingParticle().nameAsComponent())
                        .lore(getConfiguration().getPlayerSelectingParticle().loreAsComponent())
                        .asGuiItem();

                gui.setItem(getConfiguration().getPlayerSelectingParticle().slot(), particleButton);

                final GuiItem materialButton = ItemBuilder
                        .from(getConfiguration().getPlayerSelectingMaterial().itemStack())
                        .name(getConfiguration().getPlayerSelectingMaterial().nameAsComponent())
                        .lore(getConfiguration().getPlayerSelectingMaterial().loreAsComponent())
                        .asGuiItem(event -> handler.getMaterialSelector().openFor(player));

                gui.setItem(getConfiguration().getPlayerSelectingMaterial().slot(), materialButton);

                final GuiItem setBackButton = ItemBuilder
                        .from(getConfiguration().getPlayerSelectingSetBack().itemStack())
                        .name(getConfiguration().getPlayerSelectingSetBack().nameAsComponent())
                        .lore(getConfiguration().getPlayerSelectingSetBack().loreAsComponent())
                        .asGuiItem(event -> {
                            handler.getSetBackSelector().openFor(player);
                        });

                gui.setItem(getConfiguration().getPlayerSelectingSetBack().slot(), setBackButton);
                return gui;
            }
        });
    }
}

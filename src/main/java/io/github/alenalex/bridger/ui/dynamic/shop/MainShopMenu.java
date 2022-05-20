package io.github.alenalex.bridger.ui.dynamic.shop;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import io.github.alenalex.bridger.abstracts.AbstractStaticGUI;
import io.github.alenalex.bridger.handler.UIHandler;
import org.bukkit.entity.Player;

public class MainShopMenu extends AbstractStaticGUI<Gui> {

    public MainShopMenu(UIHandler handler) {
        super(handler);
    }

    @Override
    public boolean initGui() {
        try {
            gui = Gui
                    .gui()
                    .rows(getConfiguration().getPlayerShopMainConfig().rows())
                    .disableAllInteractions()
                    .title(getConfiguration().getPlayerShopMainConfig().titleAsComponent())
                    .create();

            applyFiller(gui, getConfiguration().getParticleShopConfig());

            final GuiItem materialButton = ItemBuilder
                    .from(getConfiguration().getPlayerShopMaterialItem().itemStack())
                    .name(getConfiguration().getPlayerShopMaterialItem().nameAsComponent())
                    .lore(getConfiguration().getPlayerShopMaterialItem().loreAsComponent())
                    .asGuiItem(event -> {
                        handler.getMaterialsShop().openFor((Player) event.getWhoClicked());
                    });

            gui.setItem(getConfiguration().getPlayerShopMaterialItem().slot(), materialButton);

            final GuiItem particleButton = ItemBuilder
                    .from(getConfiguration().getPlayerShopParticleItem().itemStack())
                    .name(getConfiguration().getPlayerShopParticleItem().nameAsComponent())
                    .lore(getConfiguration().getPlayerShopParticleItem().loreAsComponent())
                    .asGuiItem(event -> {
                        handler.getParticleShop().openFor((Player) event.getWhoClicked());
                    });
            gui.setItem(getConfiguration().getPlayerShopParticleItem().slot(), particleButton);

            final GuiItem fireWorkButton = ItemBuilder
                    .from(getConfiguration().getPlayerShopFireWorkItem().itemStack())
                    .name(getConfiguration().getPlayerShopFireWorkItem().nameAsComponent())
                    .lore(getConfiguration().getPlayerShopFireWorkItem().loreAsComponent())
                    .asGuiItem(event -> {
                        handler.getFireworkShop().openFor((Player) event.getWhoClicked());
                    });
            gui.setItem(getConfiguration().getPlayerShopFireWorkItem().slot(), fireWorkButton);

            final GuiItem closeButton = ItemBuilder
                    .from(getConfiguration().getPlayerShopCloseItem().itemStack())
                    .name(getConfiguration().getPlayerShopCloseItem().nameAsComponent())
                    .lore(getConfiguration().getPlayerShopCloseItem().loreAsComponent())
                    .asGuiItem(event ->  {
                        gui.close(event.getWhoClicked());
                    });
            gui.setItem(getConfiguration().getPlayerShopCloseItem().slot(), closeButton);

            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void reload() {

    }
}

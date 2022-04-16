package io.github.alenalex.bridger.gui.statics;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.GuiAction;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import io.github.alenalex.bridger.abstracts.AbstractStaticGUI;
import io.github.alenalex.bridger.gui.config.UIFiller;
import io.github.alenalex.bridger.handler.UIHandler;
import io.github.alenalex.bridger.utils.adventure.MessageFormatter;
import net.kyori.adventure.text.Component;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class CosmeticShop extends AbstractStaticGUI<Gui> {

    public CosmeticShop(UIHandler handler) {
        super(handler);
    }

    @Override
    public boolean initGui() {
        try {

            final UIFiller blackStainedFiller = new UIFiller(
                    new ItemStack(Material.STAINED_GLASS_PANE,
                            1,
                            DyeColor.BLACK.getData()
                    ),
                    new ArrayList<Integer>(){{

                    }}
            );

            gui = Gui.gui()
                    .disableAllInteractions()
                    .title(MessageFormatter.convertToComponent("<red>Cosmetics"))
                    .rows(3)
                    .create();

            applyFiller(gui, blackStainedFiller);

            final GuiItem closeButton = ItemBuilder
                    .from(Material.BARRIER)
                    .name(MessageFormatter.convertToComponent("<red>Close Menu"))
                    .lore(
                            MessageFormatter.convertToComponent("<gray>Click to close this menu")
                    )
                    .asGuiItem(new GuiAction<InventoryClickEvent>() {
                        @Override
                        public void execute(InventoryClickEvent event) {
                            gui.close(event.getWhoClicked());
                        }
                    });

            final GuiItem fireWorkShopButton = ItemBuilder
                    .from(Material.FIREWORK)
                    .name(MessageFormatter.convertToComponent("<aqua>Fireworks"))
                    .lore(
                            MessageFormatter.convertToComponent("<gray>Click to open fireworks shop"),                            Component.empty(),
                            Component.empty(),
                            MessageFormatter.convertToComponent("<white>Fireworks are extra cosmetics"),
                            MessageFormatter.convertToComponent("<white>which are fired once you "),
                            MessageFormatter.convertToComponent("<white>successfully completed a round!"),
                            Component.empty(),
                            MessageFormatter.convertToComponent("<white>There are various types of burst models "),
                            MessageFormatter.convertToComponent("<white>from which you can purchase.")
                    ).asGuiItem(new GuiAction<InventoryClickEvent>() {
                        @Override
                        public void execute(InventoryClickEvent event) {
                            if(event.getWhoClicked() == null)
                                return;

                            gui.close(event.getWhoClicked());
                            handler.getCosmeticShop().openFor((Player) event.getWhoClicked());
                        }
                    });

                gui.setItem(12, fireWorkShopButton);
                gui.setItem(22, closeButton);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void reload() {

    }
}

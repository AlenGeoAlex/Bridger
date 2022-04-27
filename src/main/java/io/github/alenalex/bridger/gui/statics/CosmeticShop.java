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

import java.util.Arrays;

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
                    Arrays.asList(0,1,2,3,4,5,6,7,8,9,17,18,19,20,21,23,24,25,26)
            );

            final UIFiller whiteStainedGlass = new UIFiller(
                    new ItemStack(Material.STAINED_GLASS_PANE,
                            1,
                            DyeColor.WHITE.getData()
                    ),
                    Arrays.asList(10,11,15,16)
            );

            gui = Gui.gui()
                    .disableAllInteractions()
                    .title(MessageFormatter.convertToComponent("<red>Cosmetics"))
                    .rows(3)
                    .create();

            applyFiller(gui, Arrays.asList( blackStainedFiller, whiteStainedGlass));

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
                            MessageFormatter.convertToComponent("<gray>Click to open fireworks shop"),
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
                            gui.close(event.getWhoClicked());

                            handler.getFireworkShop().openFor((Player) event.getWhoClicked());
                        }
                    });

            final GuiItem particleShop = ItemBuilder
                    .from(Material.REDSTONE)
                    .name(MessageFormatter.convertToComponent("<aqua>Particles"))
                    .lore(
                            MessageFormatter.convertToComponent("<gray>Click to open particles shop"),
                            Component.empty(),
                            MessageFormatter.convertToComponent("<white>Particles are cosmetics"),
                            MessageFormatter.convertToComponent("<white>which are shown along the path"),
                            MessageFormatter.convertToComponent("<white>of the placed blocks when your"),
                            MessageFormatter.convertToComponent("<white>island is put to reset the blocks you placed"),
                            Component.empty(),
                            MessageFormatter.convertToComponent("<white>There are various particle models"),
                            MessageFormatter.convertToComponent("<white>from which you can purchase.")
                    ).asGuiItem(new GuiAction<InventoryClickEvent>() {
                        @Override
                        public void execute(InventoryClickEvent event) {
                            gui.close(event.getWhoClicked());

                            handler.getFireworkShop().openFor((Player) event.getWhoClicked());
                        }
                    });

            final GuiItem materialShop = ItemBuilder
                    .from(Material.BED_BLOCK)
                    .name(MessageFormatter.convertToComponent("<aqua>Materials"))
                    .lore(
                            MessageFormatter.convertToComponent("<gray>Click to open material shop"),
                            Component.empty(),
                            MessageFormatter.convertToComponent("<white>Materials are cosmetics"),
                            MessageFormatter.convertToComponent("<white>on which you practice building blocks on!"),
                            Component.empty(),
                            MessageFormatter.convertToComponent("<white>There are various different materials"),
                            MessageFormatter.convertToComponent("<white>from which you can purchase.")
                    ).asGuiItem(new GuiAction<InventoryClickEvent>() {
                        @Override
                        public void execute(InventoryClickEvent event) {
                            gui.close(event.getWhoClicked());

                            handler.getMaterialsShop().openFor((Player) event.getWhoClicked());
                        }
                    });

                gui.setItem(12, fireWorkShopButton);
                gui.setItem(22, closeButton);
                gui.setItem(13, particleShop);
                gui.setItem(14, materialShop);
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

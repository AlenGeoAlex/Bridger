package io.github.alenalex.bridger.gui.statics;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import io.github.alenalex.bridger.abstracts.AbstractStaticGUI;
import io.github.alenalex.bridger.gui.config.UIFiller;
import io.github.alenalex.bridger.handler.UIHandler;
import io.github.alenalex.bridger.models.player.UserData;
import io.github.alenalex.bridger.utils.adventure.MessageFormatter;
import io.github.alenalex.bridger.utils.adventure.internal.MessagePlaceholder;
import io.github.alenalex.bridger.variables.LangConfigurationPaths;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class SetBackSelector extends AbstractStaticGUI<Gui> {

    public SetBackSelector(UIHandler handler) {
        super(handler);
    }

    @Override
    public boolean initGui() {
        gui = Gui.gui()
                .disableAllInteractions()
                .rows(4)
                .title(MessageFormatter.transform("<red>Setback selector"))
                .create();

        final UIFiller blackStainedFiller = new UIFiller(
                new ItemStack(Material.STAINED_GLASS_PANE,
                        1,
                        DyeColor.BLACK.getData()
                ),
                Arrays.asList(0,1,2,3,4,5,6,7,8,9,17,18,19,20,21,22,23,24,25,26)
        );

        applyFiller(this.gui, blackStainedFiller);

        try {
            final GuiItem addOne = ItemBuilder
                    .from(Material.FEATHER)
                    .name(MessageFormatter.transform("<green>+1"))
                    .amount(1)
                    .lore(Arrays.asList(
                            MessageFormatter.transform("<gray>Increase <green>+1<gray> seconds to"),
                            MessageFormatter.transform("<gray>your setback count!")
                    ))
                    .asGuiItem(event ->  {
                        final Player player = (Player) event.getWhoClicked();
                        if(player == null)
                            return;

                        final UserData userData = handler.plugin().gameHandler().userManager().of(player.getUniqueId());
                        if(userData == null)
                            return;

                        userData.userSettings().incrementSetBack();
                        handler.plugin().messagingUtils().sendTo(player, userData.userSettings().getLanguage().asComponent(LangConfigurationPaths.SETBACK_SET,
                                MessagePlaceholder.of("%value%", userData.userSettings().getSetBack()),
                                MessagePlaceholder.of("%name%", player.getName())
                        ));
                    });

            final GuiItem addFive = ItemBuilder
                    .from(Material.FEATHER)
                    .name(MessageFormatter.transform("<green>+5"))
                    .amount(5)
                    .lore(Arrays.asList(
                            MessageFormatter.transform("<gray>Increase <green>+5<gray> seconds to "),
                            MessageFormatter.transform("<gray>your setback count!")
                    ))
                    .asGuiItem(event ->  {
                        final Player player = (Player) event.getWhoClicked();
                        if(player == null)
                            return;

                        final UserData userData = handler.plugin().gameHandler().userManager().of(player.getUniqueId());
                        if(userData == null)
                            return;

                        userData.userSettings().incrementSetBackBy(5);
                        handler.plugin().messagingUtils().sendTo(player, userData.userSettings().getLanguage().asComponent(LangConfigurationPaths.SETBACK_SET,
                                MessagePlaceholder.of("%value%", userData.userSettings().getSetBack()),
                                MessagePlaceholder.of("%name%", player.getName())
                        ));
                    });

            final GuiItem addTen = ItemBuilder
                    .from(Material.FEATHER)
                    .name(MessageFormatter.transform("<green>+10"))
                    .amount(10)
                    .lore(Arrays.asList(
                            MessageFormatter.transform("<gray>Increase <green>+10<gray> seconds to"),
                            MessageFormatter.transform("<gray>your setback count!")
                    ))
                    .asGuiItem(event ->  {
                        final Player player = (Player) event.getWhoClicked();
                        if(player == null)
                            return;

                        final UserData userData = handler.plugin().gameHandler().userManager().of(player.getUniqueId());
                        if(userData == null)
                            return;

                        userData.userSettings().incrementSetBackBy(10);
                        handler.plugin().messagingUtils().sendTo(player, userData.userSettings().getLanguage().asComponent(LangConfigurationPaths.SETBACK_SET,
                                MessagePlaceholder.of("%value%", userData.userSettings().getSetBack()),
                                MessagePlaceholder.of("%name%", player.getName())
                        ));
                    });

            final GuiItem addSixty = ItemBuilder
                    .from(Material.FEATHER)
                    .name(MessageFormatter.transform("<green>+60"))
                    .amount(60)
                    .lore(Arrays.asList(
                            MessageFormatter.transform("<gray>Increase <green>+60<gray> seconds to"),
                            MessageFormatter.transform("<gray>your setback count!")
                    ))
                    .asGuiItem(event ->  {
                        final Player player = (Player) event.getWhoClicked();
                        if(player == null)
                            return;

                        final UserData userData = handler.plugin().gameHandler().userManager().of(player.getUniqueId());
                        if(userData == null)
                            return;

                        userData.userSettings().incrementSetBackBy(60);
                        handler.plugin().messagingUtils().sendTo(player, userData.userSettings().getLanguage().asComponent(LangConfigurationPaths.SETBACK_SET,
                                MessagePlaceholder.of("%value%", userData.userSettings().getSetBack()),
                                MessagePlaceholder.of("%name%", player.getName())
                        ));
                    });

            final GuiItem removeOne = ItemBuilder
                    .from(Material.FEATHER)
                    .name(MessageFormatter.transform("<red>-1"))
                    .amount(1)
                    .lore(Arrays.asList(
                            MessageFormatter.transform("<gray>Increase <red>-1<gray> seconds to"),
                            MessageFormatter.transform("<gray>your setback count!")
                    ))
                    .asGuiItem(event ->  {
                        final Player player = (Player) event.getWhoClicked();
                        if(player == null)
                            return;

                        final UserData userData = handler.plugin().gameHandler().userManager().of(player.getUniqueId());
                        if(userData == null)
                            return;

                        userData.userSettings().decrementSetBack();
                        handler.plugin().messagingUtils().sendTo(player, userData.userSettings().getLanguage().asComponent(LangConfigurationPaths.SETBACK_SET,
                                MessagePlaceholder.of("%value%", userData.userSettings().getSetBack()),
                                MessagePlaceholder.of("%name%", player.getName())
                        ));
                    });

            final GuiItem removeFive = ItemBuilder
                    .from(Material.FEATHER)
                    .name(MessageFormatter.transform("<red>-5"))
                    .amount(5)
                    .lore(Arrays.asList(
                            MessageFormatter.transform("<gray>Increase <red>-5<gray> seconds to "),
                            MessageFormatter.transform("<gray>your setback count!")
                    ))
                    .asGuiItem(event ->  {
                        final Player player = (Player) event.getWhoClicked();
                        if(player == null)
                            return;

                        final UserData userData = handler.plugin().gameHandler().userManager().of(player.getUniqueId());
                        if(userData == null)
                            return;

                        userData.userSettings().decrementSetBackBy(5);
                        handler.plugin().messagingUtils().sendTo(player, userData.userSettings().getLanguage().asComponent(LangConfigurationPaths.SETBACK_SET,
                                MessagePlaceholder.of("%value%", userData.userSettings().getSetBack()),
                                MessagePlaceholder.of("%name%", player.getName())
                        ));
                    });

            final GuiItem removeTen = ItemBuilder
                    .from(Material.FEATHER)
                    .name(MessageFormatter.transform("<green>-10"))
                    .amount(10)
                    .lore(Arrays.asList(
                            MessageFormatter.transform("<gray>Increase <red>-10<gray> seconds to"),
                            MessageFormatter.transform("<gray>your setback count!")
                    ))
                    .asGuiItem(event ->  {
                        final Player player = (Player) event.getWhoClicked();
                        if(player == null)
                            return;

                        final UserData userData = handler.plugin().gameHandler().userManager().of(player.getUniqueId());
                        if(userData == null)
                            return;

                        userData.userSettings().decrementSetBackBy(10);
                        handler.plugin().messagingUtils().sendTo(player, userData.userSettings().getLanguage().asComponent(LangConfigurationPaths.SETBACK_SET,
                                MessagePlaceholder.of("%value%", userData.userSettings().getSetBack()),
                                MessagePlaceholder.of("%name%", player.getName())
                        ));
                    });

            final GuiItem removeSixty = ItemBuilder
                    .from(Material.FEATHER)
                    .name(MessageFormatter.transform("<red>-60"))
                    .amount(60)
                    .lore(Arrays.asList(
                            MessageFormatter.transform("<gray>Increase <red>-60<gray> seconds to"),
                            MessageFormatter.transform("<gray>your setback count!")
                    ))
                    .asGuiItem(event ->  {
                        final Player player = (Player) event.getWhoClicked();
                        if(player == null)
                            return;

                        final UserData userData = handler.plugin().gameHandler().userManager().of(player.getUniqueId());
                        if(userData == null)
                            return;

                        userData.userSettings().decrementSetBackBy(60);
                        handler.plugin().messagingUtils().sendTo(player, userData.userSettings().getLanguage().asComponent(LangConfigurationPaths.SETBACK_SET,
                                MessagePlaceholder.of("%value%", userData.userSettings().getSetBack()),
                                MessagePlaceholder.of("%name%", player.getName())
                        ));
                    });

            final GuiItem resetSetBack = ItemBuilder
                    .from(Material.HOPPER)
                    .name(MessageFormatter.transform("<red>Reset Setback"))
                    .lore(MessageFormatter.transform("<gray>Reset your active setback"))
                    .asGuiItem(event -> {
                        final Player player = (Player) event.getWhoClicked();
                        if(player == null)
                            return;

                        final UserData userData = handler.plugin().gameHandler().userManager().of(player.getUniqueId());
                        if(userData == null)
                            return;

                        userData.userSettings().resetSetBack();
                        handler.plugin().messagingUtils().sendTo(player, userData.userSettings().getLanguage().asComponent(LangConfigurationPaths.SETBACK_REMOVED,
                                MessagePlaceholder.of("%value%", userData.userSettings().getSetBack()),
                                MessagePlaceholder.of("%name%", player.getName())
                        ));
                    });

            gui.setItem(11, addOne);
            gui.setItem(12, addFive);
            gui.setItem(20, addTen);
            gui.setItem(21, addSixty);
            gui.setItem(22, resetSetBack);
            gui.setItem(14, removeOne);
            gui.setItem(15, removeFive);
            gui.setItem(23, removeTen);
            gui.setItem(24, removeSixty);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void reload() {

    }
}

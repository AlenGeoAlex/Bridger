package io.github.alenalex.bridger.ui.dynamic.cosmetics;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.GuiAction;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import io.github.alenalex.bridger.abstracts.AbstractDynamicGUI;
import io.github.alenalex.bridger.handler.UIHandler;
import io.github.alenalex.bridger.models.player.UserData;
import io.github.alenalex.bridger.models.player.UserMatchCache;
import io.github.alenalex.bridger.utils.adventure.MessageFormatter;
import io.github.alenalex.bridger.utils.adventure.internal.MessagePlaceholder;
import io.github.alenalex.bridger.variables.LangConfigurationPaths;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public final class MaterialSelector extends AbstractDynamicGUI<PaginatedGui> {

    public MaterialSelector(UIHandler handler) {
        super(handler);
    }

    @Override
    public CompletableFuture<PaginatedGui> prepGui(@NotNull Player player, Object... params) {
        return CompletableFuture.supplyAsync(new Supplier<PaginatedGui>() {
            @Override
            public PaginatedGui get() {

                final UserData data = handler.plugin().gameHandler().userManager().getOrDefault(player.getUniqueId(), null);
                if (data == null) {
                    handler.plugin().getLogger().warning("User data is null for player " + player.getName()+", at prepGui()");
                    return null;
                }

                if(!(data.userMatchCache().getStatus() == UserMatchCache.Status.LOBBY)){
                    handler.plugin().messagingUtils().sendTo(
                            player,
                            data.userSettings().getLanguage().asComponent(LangConfigurationPaths.ACTIVITY_BLOCKED)
                    );
                    return null;
                }

                final PaginatedGui gui = Gui
                        .paginated()
                        .disableAllInteractions()
                        .title(getConfiguration().getMaterialSelectorConfig().titleAsComponent())
                        .rows(getConfiguration().getIslandSelectorConfig().rows())
                        .create();

                applyFiller(gui, getConfiguration().getMaterialSelectorConfig());

                final GuiItem nextItem = ItemBuilder
                        .from(getConfiguration().getMaterialSelectorNext().itemStack())
                        .name(getConfiguration().getMaterialSelectorNext().nameAsComponent())
                        .lore(getConfiguration().getMaterialSelectorNext().loreAsComponent(
                                MessagePlaceholder.of("%page%", gui.getCurrentPageNum()),
                                MessagePlaceholder.of("%total%", gui.getNextPageNum())
                        ))
                        .asGuiItem(new GuiAction<InventoryClickEvent>() {
                            @Override
                            public void execute(InventoryClickEvent event) {
                                gui.next();
                            }
                        });

                gui.setItem(getConfiguration().getMaterialSelectorNext().slot(), nextItem);

                final GuiItem preItem = ItemBuilder
                        .from(getConfiguration().getMaterialSelectorPre().itemStack())
                        .name(getConfiguration().getMaterialSelectorPre().nameAsComponent())
                        .lore(getConfiguration().getMaterialSelectorPre().loreAsComponent(
                                MessagePlaceholder.of("%page%", gui.getCurrentPageNum()),
                                MessagePlaceholder.of("%total%", gui.getNextPageNum())
                        ))
                        .asGuiItem(new GuiAction<InventoryClickEvent>() {
                            @Override
                            public void execute(InventoryClickEvent event) {
                                gui.previous();
                            }
                        });

                gui.setItem(getConfiguration().getMaterialSelectorPre().slot(), preItem);



                final Material currentMaterial = data.userSettings().getCurrentBlock().getType();
                for(String materialUnlocked : data.userCosmetics().getMaterialUnlocked()){
                    if(!EnumUtils.isValidEnum(Material.class, materialUnlocked))
                        continue;

                    final Material material = Material.getMaterial(materialUnlocked);
                    final String name = WordUtils.capitalize(material.name().toLowerCase()).replace("_"," ");
                    final GuiItem item;
                    if(material == currentMaterial){
                         item = ItemBuilder
                                .from(material)
                                .name(getConfiguration().getMaterialSelectorCurrent().nameAsComponent(
                                        MessagePlaceholder.of("%name", name)
                                ))
                                .lore(getConfiguration().getMaterialSelectorCurrent().loreAsComponent(

                                ))
                                .glow(getConfiguration().getMaterialSelectorCurrent().shouldGlow())
                                .asGuiItem();
                    }else{
                        item = ItemBuilder
                                .from(material)
                                .name(getConfiguration().getMaterialSelectorButton().nameAsComponent(
                                        MessagePlaceholder.of("%name", name)
                                ))
                                .lore(getConfiguration().getMaterialSelectorButton().loreAsComponent(

                                ))
                                .glow(getConfiguration().getMaterialSelectorButton().shouldGlow())
                                .asGuiItem(event -> {
                                    data.userSettings().setMaterial(material.name());
                                    handler.plugin().messagingUtils().sendTo(player, data.userSettings().getLanguage().asComponent(LangConfigurationPaths.MATERIAL_SELECTED, MessagePlaceholder.of("%material-name%", name)));
                                });
                    }

                    gui.addItem(item);
                }
                return gui;
            }
        });
    }
}

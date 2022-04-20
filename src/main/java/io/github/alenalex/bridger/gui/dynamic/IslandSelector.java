package io.github.alenalex.bridger.gui.dynamic;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.GuiAction;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import io.github.alenalex.bridger.abstracts.AbstractDynamicGUI;
import io.github.alenalex.bridger.handler.UIHandler;
import io.github.alenalex.bridger.models.Island;
import io.github.alenalex.bridger.models.player.UserData;
import io.github.alenalex.bridger.models.player.UserMatchCache;
import io.github.alenalex.bridger.utils.adventure.internal.MessagePlaceholder;
import io.github.alenalex.bridger.variables.LangConfigurationPaths;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class IslandSelector extends AbstractDynamicGUI<PaginatedGui> {

    public IslandSelector(UIHandler handler) {
        super(handler);
    }

    @Override
    public CompletableFuture<PaginatedGui> prepGui(@NotNull Player player, Object... params) {
        return CompletableFuture.supplyAsync(new Supplier<PaginatedGui>() {
            @Override
            public PaginatedGui get() {
                final UserData data = handler.plugin().gameHandler().userManager().of(player.getUniqueId());
                if(data == null) {
                    player.sendMessage(ChatColor.RED+"Failed to open GUI, Please re-login again");
                    return null;
                }

                if(!(data.userMatchCache().getStatus() == UserMatchCache.Status.LOBBY)){
                    handler.plugin().messagingUtils().sendTo(
                            player,
                            data.userSettings().getLanguage().asComponent(LangConfigurationPaths.ACTIVITY_BLOCKED)
                    );
                    return null;
                }


                final PaginatedGui gui = Gui.
                        paginated().
                        rows(getConfiguration().getIslandSelectorConfig().rows()).
                        disableAllInteractions().
                        title(getConfiguration().getIslandSelectorConfig().titleAsComponent()).
                        create();

                applyFiller(gui, getConfiguration().getIslandSelectorConfig());

                final GuiItem specItem = ItemBuilder
                        .from(getConfiguration().getIslandSelectorSpectateItem().itemStack())
                        .name(getConfiguration().getIslandSelectorSpectateItem().nameAsComponent())
                        .lore(getConfiguration().getIslandSelectorSpectateItem().loreAsComponent())
                        .asGuiItem(new GuiAction<InventoryClickEvent>() {
                            @Override
                            public void execute(InventoryClickEvent event) {

                            }
                        });

                gui.setItem(getConfiguration().getIslandSelectorSpectateItem().slot(), specItem);

                final GuiItem nextItem = ItemBuilder
                        .from(getConfiguration().getIslandSelectorNextPage().itemStack())
                        .name(getConfiguration().getIslandSelectorNextPage().nameAsComponent())
                        .lore(getConfiguration().getIslandSelectorNextPage().loreAsComponent(
                                MessagePlaceholder.of("%page%", gui.getCurrentPageNum()),
                                MessagePlaceholder.of("%total%", gui.getNextPageNum())
                        ))
                        .asGuiItem(new GuiAction<InventoryClickEvent>() {
                            @Override
                            public void execute(InventoryClickEvent event) {
                                gui.next();
                            }
                        });

                gui.setItem(getConfiguration().getIslandSelectorNextPage().slot(), nextItem);

                final GuiItem preItem = ItemBuilder
                        .from(getConfiguration().getIslandSelectorPreviousPage().itemStack())
                        .name(getConfiguration().getIslandSelectorPreviousPage().nameAsComponent())
                        .lore(getConfiguration().getIslandSelectorPreviousPage().loreAsComponent(
                                MessagePlaceholder.of("%page%", gui.getCurrentPageNum()),
                                MessagePlaceholder.of("%total%", gui.getNextPageNum())
                        ))
                        .asGuiItem(new GuiAction<InventoryClickEvent>() {
                            @Override
                            public void execute(InventoryClickEvent event) {
                                gui.previous();
                            }
                        });

                gui.setItem(getConfiguration().getIslandSelectorPreviousPage().slot(), preItem);

                for(Island island : handler.plugin().gameHandler().islandManager().getAllFreeIslands(player)){

                    final GuiItem item = ItemBuilder
                            .from(getConfiguration().getIslandSelectorItem().itemStack())
                            .name(getConfiguration().getIslandSelectorItem().nameAsComponent(
                                    MessagePlaceholder.of("%name%",island.getIslandName())
                            ))
                            .lore(getConfiguration().getIslandSelectorItem().loreAsComponent(
                                    MessagePlaceholder.of("%cost%",island.getJoinCost())
                            ))
                            .asGuiItem(new GuiAction<InventoryClickEvent>() {
                                @Override
                                public void execute(InventoryClickEvent event) {
                                    if(!island.isIslandIdle()){
                                        handler.plugin().messagingUtils().sendTo(
                                                player,
                                                data.userSettings().getLanguage().asComponent(LangConfigurationPaths.ISLAND_OCCUPIED_GUI)
                                        );
                                        gui.update();
                                        return;
                                    }

                                    handler.plugin().gameHandler().toIsland(player, data, island);
                                }
                            });


                    gui.addItem(item);
                }
                return gui;
            }
        });
    }
}

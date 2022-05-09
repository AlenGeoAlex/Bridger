package io.github.alenalex.bridger.ui.dynamic.shop;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.GuiAction;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import io.github.alenalex.bridger.abstracts.AbstractDynamicGUI;
import io.github.alenalex.bridger.ui.config.UIItem;
import io.github.alenalex.bridger.handler.UIHandler;
import io.github.alenalex.bridger.models.player.UserData;
import io.github.alenalex.bridger.models.player.UserMatchCache;
import io.github.alenalex.bridger.utils.adventure.internal.MessagePlaceholder;
import io.github.alenalex.bridger.variables.LangConfigurationPaths;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public final class MaterialsShop extends AbstractDynamicGUI<PaginatedGui> {

    public MaterialsShop(UIHandler handler) {
        super(handler);
    }

    @Override
    public CompletableFuture<PaginatedGui> prepGui(@NotNull Player player, Object... params) {
        return CompletableFuture.supplyAsync(new Supplier<PaginatedGui>() {
            @Override
            public PaginatedGui get() {
                try {
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
                            .title(getConfiguration().getMaterialShopConfig().titleAsComponent())
                            .rows(getConfiguration().getMaterialShopConfig().rows())
                            .create();

                    applyFiller(gui, getConfiguration().getFireWorkShopConfig());

                    final UIItem itemConfig = getConfiguration().getMaterialShopItem();

                    for(ItemStack stack : data.fetchLockedMaterials()){
                        final String name = WordUtils.capitalize(stack.getType().name().toLowerCase()).replace("_"," ");
                        final int amount = handler.plugin().configurationHandler().getConfigurationFile().getEnabledMaterials().get(stack);

                        final GuiItem button = ItemBuilder.from(stack)
                                .name(itemConfig.nameAsComponent(
                                        MessagePlaceholder.of("%name%", name)
                                ))
                                .lore(
                                        itemConfig.loreAsComponent(
                                                MessagePlaceholder.of("%name%", name),
                                                MessagePlaceholder.of("%cost%", amount)
                                        )
                                ).asGuiItem(new GuiAction<InventoryClickEvent>() {
                                    @Override
                                    public void execute(InventoryClickEvent event) {

                                        if(!handler.plugin().pluginHookManager().getEconomyProvider().hasBalance((Player) event.getWhoClicked(), amount)){
                                            handler.plugin().messagingUtils().sendTo(
                                                    player,
                                                    data.userSettings().getLanguage().asComponent(LangConfigurationPaths.SHOP_PURCHASE_FAIL_NO_CASH,
                                                            MessagePlaceholder.of("%item-name%", name)
                                                    )
                                            );
                                            return;
                                        }

                                        handler.plugin().pluginHookManager().getEconomyProvider().withdraw((Player) event.getWhoClicked(), amount);
                                        data.userCosmetics().unlockMaterial(stack.getType().name());
                                        handler.plugin().messagingUtils().sendTo(
                                                player,
                                                data.userSettings().getLanguage().asComponent(LangConfigurationPaths.SHOP_SUCCESSFULLY_PURCHASED,
                                                        MessagePlaceholder.of("%item-name%", name),
                                                        MessagePlaceholder.of("%item-price%", amount)
                                                )
                                        );
                                    }
                                });

                        gui.addItem(button);
                    }

                    return gui;
                }catch (Exception e){
                    e.printStackTrace();
                    return null;
                }
            }
        });
    }
}


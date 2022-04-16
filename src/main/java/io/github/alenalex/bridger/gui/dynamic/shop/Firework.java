package io.github.alenalex.bridger.gui.dynamic.shop;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.GuiAction;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import io.github.alenalex.bridger.abstracts.AbstractDynamicGUI;
import io.github.alenalex.bridger.gui.config.UIItem;
import io.github.alenalex.bridger.handler.UIHandler;
import io.github.alenalex.bridger.models.player.UserData;
import io.github.alenalex.bridger.utils.adventure.internal.MessagePlaceholder;
import io.github.alenalex.bridger.variables.LangConfigurationPaths;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class Firework extends AbstractDynamicGUI<Gui> {

    public Firework(UIHandler handler) {
        super(handler);
    }

    @Override
    public CompletableFuture<Gui> prepGui(@NotNull Player player, Object... params) {
        return CompletableFuture.supplyAsync(new Supplier<Gui>() {
            @Override
            public Gui get() {
                final UserData data = handler.plugin().gameHandler().userManager().getOrDefault(player.getUniqueId(), null);
                if (data == null) {
                    handler.plugin().getLogger().warning("User data is null for player " + player.getName()+", at prepGui()");
                    return null;
                }

                final Gui gui = Gui.
                        gui().
                        disableAllInteractions().
                        title(getConfiguration().getFireWorkShopConfig().titleAsComponent()).
                        create();

                applyFiller(gui, getConfiguration().getFireWorkShopConfig());

                final UIItem itemConfig = getConfiguration().getFireWorkShopItem();
                for(FireworkEffect.Type type : data.fetchLockedFireworks()){
                    final String name = StringUtils.capitalize(type.name());
                    final int amount = handler.plugin().configurationHandler().getConfigurationFile().getEnabledFireWorkModels().get(type);

                    final GuiItem button = ItemBuilder.from(itemConfig.itemStack())
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

                                    if(!handler.plugin().pluginHookManager().getEconomyProvider().hasBalance(player, amount)){
                                        handler.plugin().messagingUtils().sendTo(
                                                player,
                                                data.userSettings().getLanguage().asComponent(LangConfigurationPaths.SHOP_PURCHASE_FAIL_NO_CASH)
                                        );
                                        return;
                                    }

                                    handler.plugin().pluginHookManager().getEconomyProvider().withdraw(player, amount);
                                    data.userCosmetics().unlockFirework(type.name());
                                    handler.plugin().messagingUtils().sendTo(
                                            player,
                                            data.userSettings().getLanguage().asComponent(LangConfigurationPaths.SHOP_SUCCESSFULLY_PURCHASED,
                                                    MessagePlaceholder.of(" %item-name%", name),
                                                    MessagePlaceholder.of("%item-price%", amount)
                                                    )
                                    );
                                }
                            });

                    gui.addItem(button);
                }

                return gui;
            }
        });
    }
}

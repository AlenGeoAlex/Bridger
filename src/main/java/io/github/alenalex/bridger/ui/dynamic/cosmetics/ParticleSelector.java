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
import io.github.alenalex.bridger.utils.adventure.internal.MessagePlaceholder;
import io.github.alenalex.bridger.variables.LangConfigurationPaths;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.particle.ParticleEffect;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class ParticleSelector extends AbstractDynamicGUI<PaginatedGui> {

    public ParticleSelector(UIHandler handler) {
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
                        .title(getConfiguration().getParticleSelectorConfig().titleAsComponent())
                        .rows(getConfiguration().getParticleSelectorConfig().rows())
                        .create();

                applyFiller(gui, getConfiguration().getParticleSelectorConfig());

                final GuiItem nextItem = ItemBuilder
                        .from(getConfiguration().getParticleSelectorNext().itemStack())
                        .name(getConfiguration().getParticleSelectorNext().nameAsComponent())
                        .lore(getConfiguration().getParticleSelectorNext().loreAsComponent(
                                MessagePlaceholder.of("%page%", gui.getCurrentPageNum()),
                                MessagePlaceholder.of("%total%", gui.getNextPageNum())
                        ))
                        .asGuiItem(new GuiAction<InventoryClickEvent>() {
                            @Override
                            public void execute(InventoryClickEvent event) {
                                gui.next();
                            }
                        });

                gui.setItem(getConfiguration().getParticleSelectorNext().slot(), nextItem);

                final GuiItem preItem = ItemBuilder
                        .from(getConfiguration().getParticleSelectorPre().itemStack())
                        .name(getConfiguration().getParticleSelectorPre().nameAsComponent())
                        .lore(getConfiguration().getParticleSelectorPre().loreAsComponent(
                                MessagePlaceholder.of("%page%", gui.getCurrentPageNum()),
                                MessagePlaceholder.of("%total%", gui.getNextPageNum())
                        ))
                        .asGuiItem(new GuiAction<InventoryClickEvent>() {
                            @Override
                            public void execute(InventoryClickEvent event) {
                                gui.previous();
                            }
                        });

                gui.setItem(getConfiguration().getParticleSelectorPre().slot(), preItem);

                final GuiItem resetButton = ItemBuilder
                        .from(getConfiguration().getParticleSelectReset().itemStack())
                        .name(getConfiguration().getParticleSelectReset().nameAsComponent())
                        .lore(getConfiguration().getParticleSelectReset().loreAsComponent())
                        .glow(getConfiguration().getParticleSelectReset().shouldGlow())
                        .asGuiItem(event -> {
                            data.userSettings().setParticle(null);
                            handler.plugin().messagingUtils().sendTo(player, data.userSettings().getLanguage().asComponent(LangConfigurationPaths.PARTICLE_SELECT_RESET));
                        });

                final ParticleEffect currentParticle = data.userSettings().getParticle().orElse(null);

                for(String eachUnlockedParticle : data.userCosmetics().getParticleUnlocked()){
                    final String name = WordUtils.capitalize(eachUnlockedParticle.toLowerCase()).replace("_"," ");

                    final GuiItem item;
                    if(currentParticle != null && currentParticle.getFieldName().equals(eachUnlockedParticle)){
                        item = ItemBuilder
                                .from(getConfiguration().getParticleSelectorCurrent().itemStack())
                                .name(getConfiguration().getParticleSelectorCurrent().nameAsComponent(
                                        MessagePlaceholder.of("%name", name)
                                ))
                                .lore(getConfiguration().getParticleSelectorCurrent().loreAsComponent(

                                ))
                                .glow(getConfiguration().getParticleSelectorCurrent().shouldGlow())
                                .asGuiItem();
                    }else{
                        item = ItemBuilder
                                .from(getConfiguration().getParticleSelectorButton().itemStack())
                                .name(getConfiguration().getParticleSelectorButton().nameAsComponent(
                                        MessagePlaceholder.of("%name", name)
                                ))
                                .lore(getConfiguration().getParticleSelectorButton().loreAsComponent(

                                ))
                                .glow(getConfiguration().getParticleSelectorButton().shouldGlow())
                                .asGuiItem(event -> {
                                    data.userSettings().setParticle(eachUnlockedParticle);
                                    handler.plugin().messagingUtils().sendTo(player, data.userSettings().getLanguage().asComponent(LangConfigurationPaths.PARTICLE_SELECTED, MessagePlaceholder.of("%particle-name%", name)));
                                });
                    }

                }

                return gui;
            }
        });
    }
}

package io.github.alenalex.bridger.ui.dynamic;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import io.github.alenalex.bridger.abstracts.AbstractDynamicGUI;
import io.github.alenalex.bridger.handler.UIHandler;
import io.github.alenalex.bridger.models.leaderboard.LeaderboardPlayer;
import io.github.alenalex.bridger.ui.config.UIItem;
import io.github.alenalex.bridger.utils.adventure.internal.MessagePlaceholder;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class LeaderboardMenu extends AbstractDynamicGUI<Gui> {

    public LeaderboardMenu(UIHandler handler) {
        super(handler);
    }

    @Override
    public CompletableFuture<Gui> prepGui(@NotNull Player player, Object... params) {
        return CompletableFuture.supplyAsync(new Supplier<Gui>() {
            @Override
            public Gui get() {
                final Gui gui = Gui.gui()
                        .disableAllInteractions()
                        .title(getConfiguration().getLeaderboardMenuConfig().titleAsComponent())
                        .rows(getConfiguration().getLeaderboardMenuConfig().rows())
                        .create();

                applyFiller(gui, getConfiguration().getLeaderboardMenuConfig());


                for(int i=1;i<=10;i++){
                    final int arrayCount = i-1;
                    if(getConfiguration().getLeaderboardPlayersConfig().get(arrayCount) == null)
                        continue;

                    final LeaderboardPlayer leaderboardPlayer = handler.plugin().leaderboardManager().ofPosition(i);
                    if(leaderboardPlayer == null)
                        continue;

                    final GuiItem item = ItemBuilder
                            .from(getConfiguration().getLeaderboardPlayersConfig().get(arrayCount).itemStack())
                            .name(getConfiguration().getLeaderboardPlayersConfig().get(arrayCount).nameAsComponent(
                                    MessagePlaceholder.of("%name%", leaderboardPlayer.getPlayerName()),
                                    MessagePlaceholder.of("%pos%", leaderboardPlayer.getPos()),
                                    MessagePlaceholder.of("%best_time%", leaderboardPlayer.getBestTime())
                            ))
                            .lore(getConfiguration().getLeaderboardPlayersConfig().get(arrayCount).loreAsComponent(
                                    MessagePlaceholder.of("%name%", leaderboardPlayer.getPlayerName()),
                                    MessagePlaceholder.of("%pos%", leaderboardPlayer.getPos()),
                                    MessagePlaceholder.of("%best_time%", leaderboardPlayer.getBestTime())
                            ))
                            .asGuiItem();

                    gui.setItem(getConfiguration().getLeaderboardPlayersConfig().get(arrayCount).slot(), item);
                }

                return gui;
            }
        });
    }
}

package io.github.alenalex.bridger.abstracts;

import dev.triumphteam.gui.guis.BaseGui;
import io.github.alenalex.bridger.configs.UIConfiguration;
import io.github.alenalex.bridger.handler.UIHandler;
import io.github.alenalex.bridger.interfaces.IGui;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public abstract class AbstractDynamicGUI<T extends BaseGui> implements IGui {

    protected UIHandler handler;

    public AbstractDynamicGUI(UIHandler handler) {
        this.handler = handler;
    }

    @Override
    public void openFor(@NotNull Player player, Object... params) {
        prepGui(player, params).thenAccept(new Consumer<T>() {
            @Override
            public void accept(T t) {
                handler.plugin().getServer().getScheduler().runTask(handler.plugin(), new Runnable() {
                    @Override
                    public void run() {
                        if(t == null) {
                            handler.plugin().getLogger().warning("Failed to open GUI for " + player.getName() + "!. The plugin returned the gui as null");
                            return;
                        }

                        t.open(player);
                    }
                });
            }
        });
    }

    @Override
    public UIConfiguration getConfiguration() {
        return handler.plugin().configurationHandler().getUiConfiguration();
    }

    public abstract CompletableFuture<T> prepGui(@NotNull Player player, Object... params);
}

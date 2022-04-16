package io.github.alenalex.bridger.abstracts;

import dev.triumphteam.gui.guis.BaseGui;
import io.github.alenalex.bridger.configs.UIConfiguration;
import io.github.alenalex.bridger.exceptions.IllegalUIAccess;
import io.github.alenalex.bridger.handler.UIHandler;
import io.github.alenalex.bridger.interfaces.IGui;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public abstract class AbstractStaticGUI<T extends BaseGui> implements IGui {

    protected UIHandler handler;
    protected T gui;

    public AbstractStaticGUI(UIHandler handler) {
        this.handler = handler;
    }

    public abstract boolean initGui();

    @Override
    public void openFor(@NotNull Player player, Object... params) {
        if(gui == null)
            throw new IllegalUIAccess("The Gui has not been initialized yet!");

        handler.plugin().getServer().getScheduler().runTask(handler.plugin(), new Runnable() {
            @Override
            public void run() {
                gui.open(player);
            }
        });
    }

    @Override
    public UIConfiguration getConfiguration() {
        return handler.plugin().configurationHandler().getUiConfiguration();
    }

    public abstract void reload();
}

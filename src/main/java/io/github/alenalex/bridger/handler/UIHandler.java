package io.github.alenalex.bridger.handler;

import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.interfaces.IHandler;

public class UIHandler implements IHandler {

    private final Bridger plugin;

    public UIHandler(Bridger plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean initHandler() {
        return true;
    }

    @Override
    public void prepareHandler() {

    }

    @Override
    public Bridger plugin() {
        return plugin;
    }

    @Override
    public void disableHandler() {
        IHandler.super.disableHandler();
    }

    @Override
    public void reloadHandler() {
        IHandler.super.reloadHandler();
    }
}

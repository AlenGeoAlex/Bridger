package io.github.alenalex.bridger.abstracts;

import io.github.alenalex.bridger.manager.HookManager;

public abstract class AbstractPluginHook {

    protected HookManager manager;
    private String pluginName;

    public AbstractPluginHook(HookManager manager, String pluginName) {
        this.manager = manager;
        this.pluginName = pluginName;
        onEnable();
    }

    public abstract void onEnable();

    public abstract void onDisable();

    private String pluginName(){
        return  pluginName;
    }
}

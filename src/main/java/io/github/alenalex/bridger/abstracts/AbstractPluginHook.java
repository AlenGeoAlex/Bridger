package io.github.alenalex.bridger.abstracts;

import io.github.alenalex.bridger.manager.HookManager;

public abstract class AbstractPluginHook {

    protected HookManager manager;
    private String pluginName;

    public AbstractPluginHook(HookManager manager, String pluginName) {
        this.manager = manager;
        this.pluginName = pluginName;
        final boolean status = onEnable();
        manager.getAllHooks().put(pluginName, status);
    }

    public boolean isHookedPluginOnline(){
        return manager.getPlugin().getServer().getPluginManager().isPluginEnabled(pluginName);
    }

    public abstract boolean onEnable();

    public abstract void onDisable();

    private String pluginName(){
        return  pluginName;
    }
}

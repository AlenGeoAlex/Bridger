package io.github.alenalex.bridger.hooks.placeholders;

import io.github.alenalex.bridger.abstracts.AbstractPluginHook;
import io.github.alenalex.bridger.manager.HookManager;

public class PlaceholderAPI extends AbstractPluginHook  {

    private PlaceholderManager placeholderManager = null;


    public PlaceholderAPI(HookManager manager) {
        super(manager, "PlaceholderAPI");
    }

    @Override
    public boolean onEnable() {
        if(isHookedPluginOnline()) {
            placeholderManager = new PlaceholderManager(manager.getPlugin());
            this.placeholderManager.register();
            return true;
        }

        return false;
    }

    @Override
    public void onDisable() {
        if(placeholderManager != null && placeholderManager.isRegistered())
            placeholderManager.unregister();
    }
}

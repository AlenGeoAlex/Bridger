package io.github.alenalex.bridger.hooks.placeholders;

import io.github.alenalex.bridger.abstracts.AbstractPluginHook;
import io.github.alenalex.bridger.manager.HookManager;

public class PlaceholderAPI extends AbstractPluginHook  {

    private final PlaceholderManager placeholderManager;


    public PlaceholderAPI(HookManager manager) {
        super(manager, "PlaceholderAPI");
        this.placeholderManager = new PlaceholderManager(manager.getPlugin());
    }

    @Override
    public boolean onEnable() {
        if(isHookedPluginOnline()) {
            this.placeholderManager.register();
            return true;
        }

        return false;
    }

    @Override
    public void onDisable() {
        if(placeholderManager!= null && placeholderManager.isRegistered())
            placeholderManager.unregister();
    }
}

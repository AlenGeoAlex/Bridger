package io.github.alenalex.bridger.manager;

import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.exceptions.IllegalHookAccess;
import io.github.alenalex.bridger.hooks.VaultEconomyProvider;
import io.github.alenalex.bridger.hooks.placeholders.PlaceholderAPI;
import io.github.alenalex.bridger.interfaces.IEconomyProvider;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HookManager {

    private final Bridger plugin;
    private IEconomyProvider economyProvider;
    private PlaceholderAPI placeholderAPI;

    private final Map<String, Boolean> allHooks;

    public HookManager(Bridger plugin) {
        this.plugin = plugin;
        this.allHooks = new HashMap<>();
    }

    public boolean validateMinHookRequirements() {
        boolean economy = false;
        if(plugin.getServer().getPluginManager().isPluginEnabled("Vault"))
            economy = true;

        if(!economy){
            plugin.getLogger().severe("An economy provider isn't found, please install one of the following: Vault");
        }

        return economy;
    }

    public void registerHooks(){
        this.economyProvider = new VaultEconomyProvider(this);
        this.placeholderAPI = new PlaceholderAPI(this);
    }

    public Bridger getPlugin() {
        return plugin;
    }

    public IEconomyProvider getEconomyProvider() {
        if(economyProvider == null)
            throw new IllegalHookAccess("Economy provider isn't registered, It shouldn't happen, but seems like somethings off, Please contact the developer");

        return economyProvider;
    }

    public boolean isHookEnabled(@NotNull String hookName){
        if(!allHooks.containsKey(hookName))
            return false;

        return allHooks.get(hookName);
    }

    public Map<String, Boolean> getAllHooks() {
        return allHooks;
    }
}

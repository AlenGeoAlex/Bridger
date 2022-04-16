package io.github.alenalex.bridger.manager;

import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.exceptions.IllegalHookAccess;
import io.github.alenalex.bridger.hooks.VaultEconomyProvider;
import io.github.alenalex.bridger.interfaces.IEconomyProvider;

public class HookManager {

    private final Bridger plugin;
    private IEconomyProvider economyProvider;

    public HookManager(Bridger plugin) {
        this.plugin = plugin;
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
        economyProvider = new VaultEconomyProvider(this);
    }

    public Bridger getPlugin() {
        return plugin;
    }

    public IEconomyProvider getEconomyProvider() {
        if(economyProvider == null)
            throw new IllegalHookAccess("Economy provider isn't registered, It shouldn't happen, but seems like somethings off, Please contact the developer");

        return economyProvider;
    }


}

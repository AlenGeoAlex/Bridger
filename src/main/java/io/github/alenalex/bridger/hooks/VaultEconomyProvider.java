package io.github.alenalex.bridger.hooks;

import io.github.alenalex.bridger.abstracts.AbstractPluginHook;
import io.github.alenalex.bridger.exceptions.IllegalHookAccess;
import io.github.alenalex.bridger.interfaces.IEconomyProvider;
import io.github.alenalex.bridger.manager.HookManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;

public class VaultEconomyProvider extends AbstractPluginHook implements IEconomyProvider {

    private Economy economy;

    public VaultEconomyProvider(HookManager manager) {
        super(manager, "Vault");
    }

    @Override
    public void onEnable() {
        final RegisteredServiceProvider<Economy> registeredServiceProvider = manager.getPlugin().getServer().getServicesManager().getRegistration(Economy.class);
        if(registeredServiceProvider == null) {
            return;
        }

        economy = registeredServiceProvider.getProvider();
        manager.getPlugin().getLogger().info("Hooked into Vault!");
    }

    @Override
    public void onDisable() {

    }

    @Override
    public boolean hasBalance(@NotNull Player player, double amount) {
        if(amount < 0)
            return true;

        return economy.has(player, amount);
    }

    @Override
    public double getBalance(@NotNull Player player) {
        try {
            return economy.getBalance(player);
        }catch (Exception e){
            throw new IllegalHookAccess("The economy provider had an error while trying to get the balance of a player.", e);
        }
    }

    @Override
    public void withdraw(@NotNull Player player, double amount) {
        if(amount < 0)
            return;

        try {
            economy.withdrawPlayer(player, amount);
        }catch (Exception e){
            throw new IllegalHookAccess("The economy provider is unable to withdraw amount from the player! Check stacktrace below", e);
        }
    }

    @Override
    public void deposit(@NotNull Player player, double amount) {
        if(amount < 0)
            return;

        try {
            economy.depositPlayer(player, amount);
        }catch (Exception e){
            throw new IllegalHookAccess("The economy provider is unable to deposit amount to the player! Check stacktrace below", e);
        }
    }
}

package io.github.alenalex.bridger.commands.subcommands.player;

import dev.triumphteam.cmd.core.annotation.Default;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import io.github.alenalex.bridger.abstracts.AbstractCommand;
import io.github.alenalex.bridger.manager.CommandManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ShopCommand extends AbstractCommand {

    public ShopCommand(CommandManager manager) {
        super(manager, manager.plugin().configurationHandler().getConfigurationFile().getShopCommand());
        this.registerHelpMessage("particle", "Opens up the particle shop");
        this.registerHelpMessage("material", "Opens up the material shop");
        this.registerHelpMessage("firework", "Open up the firework shop");
    }

    @Default
    public void voidOnDefaultCommand(@NotNull Player player){
        manager.plugin().uiHandler().getMainShopMenu().openFor(player);
    }

    @SubCommand("particle")
    public void onParticleCommand(@NotNull Player player){
        manager.plugin().uiHandler().getParticleShop().openFor(player);
    }

    @SubCommand("material")
    public void onMaterialCommand(@NotNull Player player){
        manager.plugin().uiHandler().getMaterialsShop().openFor(player);
    }

    @SubCommand("firework")
    public void onFireWorkCommand(@NotNull Player player){
        manager.plugin().uiHandler().getFireworkShop().openFor(player);
    }
}

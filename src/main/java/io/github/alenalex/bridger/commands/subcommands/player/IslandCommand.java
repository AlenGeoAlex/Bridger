package io.github.alenalex.bridger.commands.subcommands.player;

import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.annotation.Async;
import dev.triumphteam.cmd.core.annotation.Default;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import io.github.alenalex.bridger.abstracts.AbstractCommand;
import io.github.alenalex.bridger.manager.CommandManager;
import io.github.alenalex.bridger.models.Island;
import io.github.alenalex.bridger.models.player.UserData;
import io.github.alenalex.bridger.variables.LangConfigurationPaths;
import io.github.alenalex.bridger.variables.Permissions;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.function.Consumer;


public final class IslandCommand extends AbstractCommand {

    public IslandCommand(CommandManager manager) {
        super(manager, manager.plugin().configurationHandler().getConfigurationFile().getIslandCommand());
        this.registerHelpMessage("island", "Join any free island available.");
        this.registerHelpMessage("help", "Shows this help menu.");
        this.registerHelpMessage("join [name]", "Joins an island with the provided name if its available!");
        this.registerHelpMessage("gui", "Opens up Island Gui");
        this.registerHelpMessage("leave","Leaves your island.");
    }

    @Default
    public void onDefaultCommand(Player player){
        if(player == null){
            return;
        }



        manager.plugin().gameHandler().toIsland(player).ifPresent(new Consumer<Island>() {
            @Override
            public void accept(Island island) {
                manager.plugin().getLogger().info("Player has been loaded on to the island "+island.getIslandName());
            }
        });
    }

    @SubCommand("help")
    @Async
    public void onHelpCommand(Player player){
        sendHelpMessage(player);
    }

    @SubCommand("join")
    @Permission(Permissions.Commands.Island.BY_NAME)
    public void onJoinByNameCommand(@NotNull Player player, String islandName){
        final UserData userData = manager.plugin().gameHandler().userManager().of(player.getUniqueId());
        if(userData == null) {
            return;
        }

        if(StringUtils.isBlank(islandName)){
            manager.plugin().messagingUtils().sendTo(player, userData.userSettings().getLanguage().asComponent(LangConfigurationPaths.NOT_PROVIDED_VALID_ISLAND_NAME));
            return;
        }

        manager.plugin().gameHandler().toIsland(player, userData ,islandName).ifPresent(new Consumer<Island>() {
            @Override
            public void accept(Island island) {
                manager.plugin().getLogger().info("Player has been loaded on to the island "+island.getIslandName());
            }
        });
    }

    @SubCommand("gui")
    @Permission(Permissions.Commands.Island.GUI)
    public void onGuiCommand(Player player){
        manager.plugin().uiHandler().getIslandSelector().openFor(player);
    }

    @SubCommand(value = "leave", alias = {"exit", "quit"})
    @Async
    public void onLeaveCommand(Player player){
        final UserData userData = manager.plugin().gameHandler().userManager().of(player.getUniqueId());
        if(userData == null) {
            return;
        }

        if(!manager.plugin().gameHandler().isPlayerPlaying(player))
            return;

        Island island = manager.plugin().gameHandler().getIslandOfPlayer(player).orElse(null);
        if(island == null){
            return;
        }
        manager.plugin().gameHandler().playerQuitGame(player, userData);
    }
}

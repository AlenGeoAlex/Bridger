package io.github.alenalex.bridger.commands;

import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Default;
import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.manager.CommandManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Command("test")
public class Test extends BaseCommand {

    private final CommandManager commandManager;

    public Test(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @Default
    public void onDefaultCommand(@NotNull Player player){
            commandManager.plugin().uiHandler().getLeaderboardMenu().openFor(player);
    }

}

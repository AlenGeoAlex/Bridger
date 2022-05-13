package io.github.alenalex.bridger.commands.player;

import dev.triumphteam.cmd.core.annotation.Default;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import io.github.alenalex.bridger.abstracts.AbstractCommand;
import io.github.alenalex.bridger.manager.CommandManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class LeaderboardCommand extends AbstractCommand {

    public LeaderboardCommand(CommandManager manager) {
        super(manager, "leaderboard");
        this.registerHelpMessage("gui", "Opens up the GUI leaderboard menu");
    }

    @Default
    public void onDefaultCommand(@NotNull final Player player){
        sendHelpMessage(player);
    }

    @SubCommand("gui")
    public void onGuiCommand(@NotNull final Player player){
        manager.plugin().uiHandler().getLeaderboardMenu().openFor(player);
    }

}

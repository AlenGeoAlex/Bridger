package io.github.alenalex.bridger.commands.subcommands.player;

import dev.triumphteam.cmd.core.annotation.Default;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import io.github.alenalex.bridger.abstracts.AbstractCommand;
import io.github.alenalex.bridger.manager.CommandManager;
import io.github.alenalex.bridger.models.player.UserData;
import io.github.alenalex.bridger.variables.LangConfigurationPaths;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public final class ScoreboardCommand extends AbstractCommand {

    public ScoreboardCommand(CommandManager manager) {
        super(manager, manager.plugin().configurationHandler().getConfigurationFile().getScoreboardCommand());
        this.registerHelpMessage("on", "Turn on the scoreboard");
        this.registerHelpMessage("off", "Turn off the scoreboard");
        this.registerHelpMessage("help", "Shows this help message");
    }

    @Default
    public void onToggleCommand(Player player){
        final UserData userData = manager.plugin().gameHandler().userManager().of(player.getUniqueId());
        if(userData == null)
            return;
        if(!userData.userSettings().isScoreboardEnabled()){
            userData.setScoreboardOn();
            manager.plugin().messagingUtils().sendTo(player, userData.userSettings().getLanguage().asComponent(LangConfigurationPaths.SCOREBOARD_ENABLED));
        }else{
            userData.setScoreboardOff();
            manager.plugin().messagingUtils().sendTo(player, userData.userSettings().getLanguage().asComponent(LangConfigurationPaths.SCOREBOARD_DISABLED));
        }
    }

    @SubCommand("on")
    public void onOnCommand(@NotNull Player player){
        final UserData userData = manager.plugin().gameHandler().userManager().of(player.getUniqueId());
        if(userData == null)
            return;

        if(userData.userSettings().isScoreboardEnabled()) {
            manager.plugin().messagingUtils().sendTo(player, userData.userSettings().getLanguage().asComponent(LangConfigurationPaths.SCOREBOARD_ALREADY_ENABLED));
            return;
        }

        userData.setScoreboardOn();
        manager.plugin().messagingUtils().sendTo(player, userData.userSettings().getLanguage().asComponent(LangConfigurationPaths.SCOREBOARD_ENABLED));
    }

    @SubCommand("off")
    public void onOffCommand(@NotNull Player player){
        final UserData userData = manager.plugin().gameHandler().userManager().of(player.getUniqueId());
        if(userData == null)
            return;

        if(!userData.userSettings().isScoreboardEnabled()) {
            manager.plugin().messagingUtils().sendTo(player, userData.userSettings().getLanguage().asComponent(LangConfigurationPaths.SCOREBOARD_ALREADY_DISABLED));
            return;
        }

        userData.setScoreboardOff();
        manager.plugin().messagingUtils().sendTo(player, userData.userSettings().getLanguage().asComponent(LangConfigurationPaths.SCOREBOARD_DISABLED));
    }

    @SubCommand("help")
    public void onHelpCommand(Player player){
        sendHelpMessage(player);
    }
}

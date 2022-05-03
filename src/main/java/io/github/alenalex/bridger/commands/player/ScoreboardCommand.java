package io.github.alenalex.bridger.commands.player;

import dev.triumphteam.cmd.core.annotation.Default;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import io.github.alenalex.bridger.abstracts.AbstractCommand;
import io.github.alenalex.bridger.manager.CommandManager;
import io.github.alenalex.bridger.models.player.BridgerUserData;
import io.github.alenalex.bridger.variables.LangConfigurationPaths;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public class ScoreboardCommand extends AbstractCommand {

    public ScoreboardCommand(CommandManager manager) {
        super(manager, "scoreboard");
        this.registerHelpMessage("on", "Turn on the scoreboard");
        this.registerHelpMessage("off", "Turn off the scoreboard");
        this.registerHelpMessage("help", "Shows this help message");
    }

    @Default
    public void onToggleCommand(Player player){
        final BridgerUserData bridgerUserData = manager.plugin().gameHandler().userManager().of(player.getUniqueId());
        if(bridgerUserData == null)
            return;
        if(!bridgerUserData.userSettings().isScoreboardEnabled()){
            bridgerUserData.setScoreboardOn();
            manager.plugin().messagingUtils().sendTo(player, bridgerUserData.userSettings().getLanguage().asComponent(LangConfigurationPaths.SCOREBOARD_ENABLED));
        }else{
            bridgerUserData.setScoreboardOff();
            manager.plugin().messagingUtils().sendTo(player, bridgerUserData.userSettings().getLanguage().asComponent(LangConfigurationPaths.SCOREBOARD_DISABLED));
        }
    }

    @SubCommand("on")
    public void onOnCommand(@NotNull Player player){
        final BridgerUserData bridgerUserData = manager.plugin().gameHandler().userManager().of(player.getUniqueId());
        if(bridgerUserData == null)
            return;

        if(bridgerUserData.userSettings().isScoreboardEnabled()) {
            manager.plugin().messagingUtils().sendTo(player, bridgerUserData.userSettings().getLanguage().asComponent(LangConfigurationPaths.SCOREBOARD_ALREADY_ENABLED));
            return;
        }

        bridgerUserData.setScoreboardOn();
        manager.plugin().messagingUtils().sendTo(player, bridgerUserData.userSettings().getLanguage().asComponent(LangConfigurationPaths.SCOREBOARD_ENABLED));
    }

    @SubCommand("off")
    public void onOffCommand(@NotNull Player player){
        final BridgerUserData bridgerUserData = manager.plugin().gameHandler().userManager().of(player.getUniqueId());
        if(bridgerUserData == null)
            return;

        if(!bridgerUserData.userSettings().isScoreboardEnabled()) {
            manager.plugin().messagingUtils().sendTo(player, bridgerUserData.userSettings().getLanguage().asComponent(LangConfigurationPaths.SCOREBOARD_ALREADY_DISABLED));
            return;
        }

        bridgerUserData.setScoreboardOff();
        manager.plugin().messagingUtils().sendTo(player, bridgerUserData.userSettings().getLanguage().asComponent(LangConfigurationPaths.SCOREBOARD_DISABLED));
    }

    @SubCommand("help")
    public void onHelpCommand(Player player){
        sendHelpMessage(player);
    }
}

package io.github.alenalex.bridger.abstracts;

import dev.triumphteam.cmd.core.BaseCommand;
import io.github.alenalex.bridger.manager.CommandManager;
import io.github.alenalex.bridger.utils.adventure.internal.MessagePlaceholder;
import io.github.alenalex.bridger.variables.PluginResponses;
import org.bukkit.entity.Player;

import java.util.Map;

public abstract class AbstractCommand extends BaseCommand {

    protected final CommandManager manager;

    public AbstractCommand(CommandManager manager) {
        this.manager = manager;
    }

    public abstract Map<String, String> getCommandDescriptionMap();

    public void sendHelpMessage(Player player){
        manager.plugin().messagingUtils().sendTo(player, PluginResponses.Commands.CommandHelpLayout.HEADER);
        getCommandDescriptionMap().forEach((command, desc) -> {
            manager.plugin().messagingUtils().sendTo(player, PluginResponses.Commands.CommandHelpLayout.HELP_COMMAND,
                    MessagePlaceholder.of("%command%", command),
                    MessagePlaceholder.of("%description%", desc)
                    );
        });
        manager.plugin().messagingUtils().sendTo(player, PluginResponses.Commands.CommandHelpLayout.FOOTER);

    }
}

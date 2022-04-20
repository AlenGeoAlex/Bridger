package io.github.alenalex.bridger.abstracts;

import dev.triumphteam.cmd.core.BaseCommand;
import io.github.alenalex.bridger.manager.CommandManager;
import io.github.alenalex.bridger.utils.adventure.internal.MessagePlaceholder;
import io.github.alenalex.bridger.variables.PluginResponses;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public abstract class AbstractCommand extends BaseCommand {

    protected final CommandManager manager;

    public AbstractCommand(CommandManager manager, @NotNull String commandName, @NotNull List<String> aliases) {
        super(commandName, aliases);
        this.manager = manager;
    }

    public AbstractCommand(CommandManager manager, @NotNull String commandName) {
        super(commandName);
        this.manager = manager;
    }

    public abstract Map<String, String> getCommandDescriptionMap();

    public void sendHelpMessage(Player player){
        if(getCommandDescriptionMap() == null || getCommandDescriptionMap().isEmpty())
            return;

        manager.plugin().messagingUtils().sendTo(player, PluginResponses.Commands.CommandHelpLayout.HEADER);
        getCommandDescriptionMap().forEach((command, desc) -> {
            manager.plugin().messagingUtils().sendTo(player, PluginResponses.Commands.CommandHelpLayout.HELP_COMMAND,
                    MessagePlaceholder.of("%command%", getCommand()+" "+command),
                    MessagePlaceholder.of("%description%", desc)
                    );
        });
        manager.plugin().messagingUtils().sendTo(player, PluginResponses.Commands.CommandHelpLayout.FOOTER);

    }
}

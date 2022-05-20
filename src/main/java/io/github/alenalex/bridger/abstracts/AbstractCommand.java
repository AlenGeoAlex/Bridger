package io.github.alenalex.bridger.abstracts;

import dev.triumphteam.cmd.core.BaseCommand;
import io.github.alenalex.bridger.commands.config.CommandInfoConfig;
import io.github.alenalex.bridger.manager.CommandManager;
import io.github.alenalex.bridger.models.player.UserData;
import io.github.alenalex.bridger.utils.adventure.internal.MessagePlaceholder;
import io.github.alenalex.bridger.variables.LangConfigurationPaths;
import io.github.alenalex.bridger.variables.PluginResponses;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractCommand extends BaseCommand {

    protected final CommandManager manager;
    private final HashMap<String, String> commandDescription;

    public AbstractCommand(CommandManager manager, @NotNull String commandName, @NotNull List<String> aliases) {
        super(commandName, aliases);
        this.manager = manager;
        this.commandDescription = new HashMap<>();
    }

    public AbstractCommand(CommandManager manager, @NotNull String commandName) {
        super(commandName);
        this.manager = manager;
        this.commandDescription = new HashMap<>();
    }

    public AbstractCommand(CommandManager manager, @NotNull CommandInfoConfig commandConfig){
        super(commandConfig.getCommandName(), commandConfig.getAliases());
        this.manager = manager;
        this.commandDescription = new HashMap<>();
    }

    public Map<String, String> getCommandDescriptionMap(){
        return commandDescription;
    }

    public void sendHelpMessage(Player player){
        if(getCommandDescriptionMap() == null || getCommandDescriptionMap().isEmpty())
            return;

        final UserData data = manager.plugin().gameHandler().userManager().of(player.getUniqueId());
        if(data == null)
            return;

        manager.plugin().messagingUtils().sendTo(player, data.userSettings().getLanguage().asComponent(LangConfigurationPaths.COMMAND_HELP_FOOTER));
        getCommandDescriptionMap().forEach((command, desc) -> {
            manager.plugin().messagingUtils().sendTo(player, data.userSettings().getLanguage().asComponent(LangConfigurationPaths.COMMAND_HELP_DESCRIPTION,
                            MessagePlaceholder.of("%command%", getCommand()+" "+command),
                            MessagePlaceholder.of("%description%", desc))
            );
        });
        manager.plugin().messagingUtils().sendTo(player, data.userSettings().getLanguage().asComponent(LangConfigurationPaths.COMMAND_HELP_FOOTER));
    }

    public void registerHelpMessage(@NotNull String commandName, @NotNull String description){
        this.commandDescription.put(commandName, description);
    }
}

package io.github.alenalex.bridger.manager;

import dev.triumphteam.cmd.bukkit.BukkitCommandManager;
import dev.triumphteam.cmd.core.message.MessageKey;
import dev.triumphteam.cmd.core.message.MessageResolver;
import dev.triumphteam.cmd.core.message.context.MessageContext;
import dev.triumphteam.cmd.core.suggestion.SuggestionContext;
import dev.triumphteam.cmd.core.suggestion.SuggestionKey;
import dev.triumphteam.cmd.core.suggestion.SuggestionResolver;
import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.commands.Test;
import io.github.alenalex.bridger.commands.admin.DebugCommand;
import io.github.alenalex.bridger.commands.island.IslandCommand;
import io.github.alenalex.bridger.commands.island.LeaveCommand;
import io.github.alenalex.bridger.commands.setup.SessionCommand;
import io.github.alenalex.bridger.models.player.UserData;
import io.github.alenalex.bridger.variables.LangConfigurationPaths;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandManager {

    private final Bridger plugin;
    private final BukkitCommandManager<CommandSender> commandManager;

    public CommandManager(Bridger plugin) {
        this.plugin = plugin;
        this.commandManager = BukkitCommandManager.create(plugin);
    }

    public void registerMessages(){
        commandManager.registerMessage(MessageKey.UNKNOWN_COMMAND, new MessageResolver<CommandSender, MessageContext>() {
            @Override
            public void resolve(@NotNull CommandSender sender, @NotNull MessageContext context) {
                if(sender instanceof Player){
                    final Player player = ((Player) sender).getPlayer();
                    final UserData data = plugin.gameHandler().userManager().of(player.getUniqueId());
                    if(data == null)
                        return;

                    plugin.messagingUtils().sendTo(player, data.userSettings().getLanguage().asComponent(LangConfigurationPaths.COMMAND_NOT_EXISTS));
                }
            }
        });

    }

    public void registerCompletions(){
        commandManager.registerSuggestion(SuggestionKey.of("joinPerm"), new SuggestionResolver<CommandSender>() {
            @Override
            public @NotNull List<String> resolve(@NotNull CommandSender sender, @NotNull SuggestionContext context) {
                return Collections.singletonList("bridger.join.");
            }
        });

        commandManager.registerSuggestion(SuggestionKey.of("activePlayers"), new SuggestionResolver<CommandSender>() {
            @Override
            public @NotNull List<String> resolve(@NotNull CommandSender sender, @NotNull SuggestionContext context) {
                return plugin.gameHandler().getActivePlayerNames();
            }
        });

    }

    public void registerCommands(){
        commandManager.registerCommand(
                new Test(this),
                new SessionCommand(this),
                new IslandCommand(this),
                new DebugCommand(this),
                new LeaveCommand(this)
        );

    }

    public Bridger plugin(){
        return plugin;
    }
}

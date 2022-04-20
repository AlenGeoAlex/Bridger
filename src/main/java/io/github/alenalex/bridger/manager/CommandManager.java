package io.github.alenalex.bridger.manager;

import dev.triumphteam.cmd.bukkit.BukkitCommandManager;
import dev.triumphteam.cmd.core.suggestion.SuggestionContext;
import dev.triumphteam.cmd.core.suggestion.SuggestionKey;
import dev.triumphteam.cmd.core.suggestion.SuggestionResolver;
import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.commands.Test;
import io.github.alenalex.bridger.commands.admin.DebugCommand;
import io.github.alenalex.bridger.commands.island.IslandCommand;
import io.github.alenalex.bridger.commands.setup.SessionCommand;
import org.bukkit.command.CommandSender;
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
                new DebugCommand(this)
        );

    }

    public Bridger plugin(){
        return plugin;
    }
}

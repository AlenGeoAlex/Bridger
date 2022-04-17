package io.github.alenalex.bridger.manager;

import dev.triumphteam.cmd.bukkit.BukkitCommandManager;
import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.commands.Test;
import io.github.alenalex.bridger.commands.setup.SessionCommand;
import org.bukkit.command.CommandSender;

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

    }

    public void registerCommands(){
        commandManager.registerCommand(
                new Test(this),
                new SessionCommand(this)
        );

    }

    public Bridger plugin(){
        return plugin;
    }
}

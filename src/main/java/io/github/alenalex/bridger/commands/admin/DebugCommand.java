package io.github.alenalex.bridger.commands.admin;

import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.annotation.*;
import io.github.alenalex.bridger.abstracts.AbstractCommand;
import io.github.alenalex.bridger.manager.CommandManager;
import io.github.alenalex.bridger.utils.PluginStats;
import io.github.alenalex.bridger.variables.Permissions;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@Command("bridgerdebug")
public class DebugCommand extends AbstractCommand {

    public DebugCommand(CommandManager manager) {
        super(manager);
    }

    @Override
    public Map<String, String> getCommandDescriptionMap() {
        return null;
    }

    @Default
    @Permission(Permissions.Commands.Debug.CREATE)
    @Async
    public void onDebugCommand(@NotNull CommandSender player, @Optional String fileName){
        final String fName = StringUtils.isBlank(fileName) ? String.valueOf(System.currentTimeMillis())+".json" : fileName;

        new PluginStats(manager.plugin()).writeAsync(fName);
    }

    @SubCommand("clear")
    @Permission(Permissions.Commands.Debug.CLEAR)
    @Async
    public void onClearCommand(@NotNull CommandSender sender){

    }
}

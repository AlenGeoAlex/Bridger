package io.github.alenalex.bridger.commands.admin;

import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.annotation.Optional;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import io.github.alenalex.bridger.abstracts.AbstractCommand;
import io.github.alenalex.bridger.manager.CommandManager;
import io.github.alenalex.bridger.utils.adventure.MessageFormatter;
import io.github.alenalex.bridger.variables.Permissions;
import io.github.alenalex.bridger.variables.PluginResponses;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class BridgerAdminCommand extends AbstractCommand {
    public BridgerAdminCommand(CommandManager manager) {
        super(manager, "bridgeradmin");
    }

    @Override
    public Map<String, String> getCommandDescriptionMap() {
        return null;
    }

    @SubCommand("reload")
    @Permission(Permissions.Commands.Admin.RELOAD)
    public void onReloadCommand(@NotNull CommandSender sender, @Optional String fileName){
        if(StringUtils.isBlank(fileName)){
            manager.plugin().prepareReloadTask();
            manager.plugin().messagingUtils().sendTo(sender, MessageFormatter.transform(PluginResponses.Commands.Admin.RELOAD_COMPLETE));
            return;
        }

        switch (fileName.toUpperCase()){
            case "CONFIG": case "CFG": case "CONFIGURATION":
                manager.plugin().configurationHandler().reloadHandler();
                manager.plugin().trackableTask().stopThread();
                manager.plugin().trackableTask().startThread();
                manager.plugin().messagingUtils().sendTo(sender, MessageFormatter.transform(PluginResponses.Commands.Admin.RELOAD_COMPLETE));
                break;
            case "LOCALE": case "LOC": case "LOCALES": case "LANGUAGE": case "LANG": case "LANGUAGES":
                if(!manager.plugin().localeManager().reloadLocaleManager()){
                    manager.plugin().getLogger().severe("Failed to load locales!");
                    manager.plugin().getLogger().severe("The plugin will be disabled!");
                    manager.plugin().getServer().getPluginManager().disablePlugin(manager.plugin());
                }
                break;
            case "ISLAND": case "ISLANDS":

               break;
            default:
                manager.plugin().messagingUtils().sendTo(sender, MessageFormatter.transform(PluginResponses.Others.UNKNOWN_CAUSE));
        }
    }
}

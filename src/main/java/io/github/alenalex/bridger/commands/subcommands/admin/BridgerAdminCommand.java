package io.github.alenalex.bridger.commands.subcommands.admin;

import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.annotation.Optional;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import dev.triumphteam.cmd.core.annotation.Suggestion;
import io.github.alenalex.bridger.abstracts.AbstractCommand;
import io.github.alenalex.bridger.manager.CommandManager;
import io.github.alenalex.bridger.models.player.UserData;
import io.github.alenalex.bridger.utils.adventure.MessageFormatter;
import io.github.alenalex.bridger.utils.adventure.internal.MessagePlaceholder;
import io.github.alenalex.bridger.variables.CommandCompletions;
import io.github.alenalex.bridger.variables.LangConfigurationPaths;
import io.github.alenalex.bridger.variables.Permissions;
import io.github.alenalex.bridger.variables.PluginResponses;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class BridgerAdminCommand extends AbstractCommand {
    public BridgerAdminCommand(CommandManager manager) {
        super(manager, "bridgeradmin");
        registerHelpMessage("reload", "Reloads the entire plugin.");
        registerHelpMessage("reload config", "Reloads only the main config.");
        registerHelpMessage("reload locale", "Reloads the locale of the plugin.");
        registerHelpMessage("reload island", "Reloads the island configuration");
        registerHelpMessage("reload scoreboard", "Reloads the scoreboard configuration");
        registerHelpMessage("build", "Allows you to build on restricted situations.");
        registerHelpMessage("build [player]", "Allows specified player to build on restricted situations.");
        registerHelpMessage("setback [player] ", "Set a players setback");
    }

    @SubCommand("reload")
    @Permission(Permissions.Commands.Admin.RELOAD)
    @Suggestion(CommandCompletions.Keys.CONFIG_RELOAD)
    public void onReloadCommand(@NotNull CommandSender sender, @Optional String fileName){
        if(StringUtils.isBlank(fileName)){
            manager.plugin().prepareReloadTask();
            manager.plugin().messagingUtils().sendTo(sender, MessageFormatter.transform(PluginResponses.Commands.Admin.RELOAD_COMPLETE));
            return;
        }

        switch (fileName.toUpperCase()){
            case "CONFIG": case "CFG": case "CONFIGURATION":
                manager.plugin().configurationHandler().getConfigurationFile().prepareReload();
                manager.plugin().configurationHandler().getConfigurationFile().loadFile();
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
                manager.plugin().messagingUtils().sendTo(sender, MessageFormatter.transform(PluginResponses.Commands.Admin.RELOAD_COMPLETE));
                break;
            case "ISLAND": case "ISLANDS":

                manager.plugin().messagingUtils().sendTo(sender, MessageFormatter.transform(PluginResponses.Commands.Admin.RELOAD_COMPLETE));
               break;
            case "SCOREBOARD": case "SB": case "SCRBRDS":
                manager.plugin().scoreboardTask().stopThread();
                manager.plugin().configurationHandler().getScoreboardConfiguration().prepareReload();
                manager.plugin().configurationHandler().getScoreboardConfiguration().loadFile();
                if(manager.plugin().configurationHandler().getScoreboardConfiguration().isScoreboardEnabled()) {
                    manager.plugin().scoreboardTask().setThreadCallPeriod(manager.plugin().configurationHandler().getScoreboardConfiguration().getScoreboardUpdateTime());
                    if (!manager.plugin().scoreboardTask().startThread()) {
                        manager.plugin().getLogger().warning("Failed to initialize thread pool for Game monitor");
                    }
                }
                manager.plugin().messagingUtils().sendTo(sender, MessageFormatter.transform(PluginResponses.Commands.Admin.RELOAD_COMPLETE));
                break;
            default:
                manager.plugin().messagingUtils().sendTo(sender, MessageFormatter.transform(PluginResponses.Others.UNKNOWN_CAUSE));
        }
    }

    @SubCommand("build")
    @Permission(Permissions.Commands.Admin.BUILD)
    @Suggestion(CommandCompletions.Keys.PLAYERS)
    public void onBuildCommand(@NotNull Player player, @Optional Player target){
        if(target == null){
            if(manager.plugin().gameHandler().userManager().isPlayerAllowedToBuild(player)){
                manager.plugin().gameHandler().userManager().removeBuildPermsToPlayer(player);
                manager.plugin().messagingUtils().sendTo(player, PluginResponses.Commands.Admin.DISABLED_BUILD);
            }else {
                manager.plugin().gameHandler().userManager().addBuildPermsToPlayer(player);
                manager.plugin().messagingUtils().sendTo(player, PluginResponses.Commands.Admin.ENABLED_BUILD);
            }
        }else {
            if(manager.plugin().gameHandler().userManager().isPlayerAllowedToBuild(target)){
                manager.plugin().gameHandler().userManager().removeBuildPermsToPlayer(target);
                manager.plugin().messagingUtils().sendTo(player, PluginResponses.Commands.Admin.DISABLED_BUILD);
                manager.plugin().messagingUtils().sendTo(target, PluginResponses.Commands.Admin.DISABLED_BUILD);
            }else {
                manager.plugin().gameHandler().userManager().addBuildPermsToPlayer(target);
                manager.plugin().messagingUtils().sendTo(player, PluginResponses.Commands.Admin.ENABLED_BUILD);
                manager.plugin().messagingUtils().sendTo(target, PluginResponses.Commands.Admin.ENABLED_BUILD);
            }
        }
    }

    @SubCommand("setback")
    @Permission(Permissions.Commands.Admin.SETBACK)
    public void onSetbackCommand(@NotNull final Player player, final Player target, Integer value){
        final UserData userData = manager.plugin().gameHandler().userManager().of(player.getUniqueId());
        if(userData == null)
            return;

        if(target == null || value == null){
            manager.plugin().messagingUtils().sendTo(player, userData.userSettings().getLanguage().asComponent(LangConfigurationPaths.COMMAND_WRONG_USAGE));
            return;
        }

        if(value <= 0){
            userData.userSettings().resetSetBack();
            manager.plugin().messagingUtils().sendTo(player, userData.userSettings().getLanguage().asComponent(LangConfigurationPaths.SETBACK_REMOVED));
        }else {
            userData.userSettings().setSetBack(value);
            manager.plugin().messagingUtils().sendTo(player, userData.userSettings().getLanguage().asComponent(LangConfigurationPaths.SETBACK_SET,
                    MessagePlaceholder.of("%value%", userData.userSettings().getSetBack()),
                    MessagePlaceholder.of("%name%", target.getName())
            ));
        }
    }


}

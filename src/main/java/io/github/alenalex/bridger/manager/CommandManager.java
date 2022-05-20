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
import io.github.alenalex.bridger.commands.subcommands.admin.BridgerAdminCommand;
import io.github.alenalex.bridger.commands.subcommands.admin.DebugCommand;
import io.github.alenalex.bridger.commands.subcommands.player.*;
import io.github.alenalex.bridger.commands.subcommands.setup.SessionCommand;
import io.github.alenalex.bridger.models.player.UserData;
import io.github.alenalex.bridger.variables.CommandCompletions;
import io.github.alenalex.bridger.variables.LangConfigurationPaths;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        commandManager.registerSuggestion(SuggestionKey.of(CommandCompletions.Keys.JOIN_PERMS), new SuggestionResolver<CommandSender>() {
            @Override
            public @NotNull List<String> resolve(@NotNull CommandSender sender, @NotNull SuggestionContext context) {
                return CommandCompletions.Parameters.ISLAND_SESSION_PERMISSION;
            }
        });

        commandManager.registerSuggestion(SuggestionKey.of(CommandCompletions.Keys.ACTIVE_PLAYERS), new SuggestionResolver<CommandSender>() {
            @Override
            public @NotNull List<String> resolve(@NotNull CommandSender sender, @NotNull SuggestionContext context) {
                return plugin.gameHandler().getActivePlayerNames();
            }
        });

        commandManager.registerSuggestion(SuggestionKey.of(CommandCompletions.Keys.CONFIG_RELOAD), new SuggestionResolver<CommandSender>() {
            @Override
            public @NotNull List<String> resolve(@NotNull CommandSender sender, @NotNull SuggestionContext context) {
                return CommandCompletions.Parameters.CONFIG_RELOAD;
            }
        });

        commandManager.registerSuggestion(SuggestionKey.of(CommandCompletions.Keys.ALL_PLAYERS), new SuggestionResolver<CommandSender>() {
            @Override
            public @NotNull List<String> resolve(@NotNull CommandSender sender, @NotNull SuggestionContext context) {
                return plugin.gameHandler()
                        .userManager()
                        .getValueCollection()
                        .stream()
                        .map(UserData::getOptionalPlayer)
                        .filter(Optional::isPresent)
                        .map(player -> player.get().getName())
                        .collect(Collectors.toList());
            }
        });

        commandManager.registerSuggestion(SuggestionKey.of(CommandCompletions.Keys.ENABLED_LOCALE), new SuggestionResolver<CommandSender>() {
            @Override
            public @NotNull List<String> resolve(@NotNull CommandSender sender, @NotNull SuggestionContext context) {
                return plugin
                        .localeManager()
                        .getModifiableKeyList();
            }
        });

        commandManager.registerSuggestion(SuggestionKey.of(CommandCompletions.Keys.PLAYERS), new SuggestionResolver<CommandSender>() {
            @Override
            public @NotNull List<String> resolve(@NotNull CommandSender sender, @NotNull SuggestionContext context) {
                return plugin
                        .getServer()
                        .getOnlinePlayers()
                        .stream()
                        .map(HumanEntity::getName)
                        .collect(Collectors.toList());
            }
        });

        commandManager.registerSuggestion(SuggestionKey.of(CommandCompletions.Keys.VALUES_1_10), new SuggestionResolver<CommandSender>() {
            @Override
            public @NotNull List<String> resolve(@NotNull CommandSender sender, @NotNull SuggestionContext context) {
                return CommandCompletions.Parameters.VALUES_1_10;
            }
        });

        commandManager.registerSuggestion(SuggestionKey.of(CommandCompletions.Keys.VALUES_SETBACK), new SuggestionResolver<CommandSender>() {
            @Override
            public @NotNull List<String> resolve(@NotNull CommandSender sender, @NotNull SuggestionContext context) {
                return CommandCompletions.Parameters.VALUES_SETBACK;
            }
        });
    }

    public void registerCommands(){
        commandManager.registerCommand(
                new Test(this),
                new SessionCommand(this),
                new DebugCommand(this),
                new BridgerAdminCommand(this)
        );

        if(plugin.configurationHandler().getConfigurationFile().getIslandCommand().isEnabled())
            new IslandCommand(this);

        if(plugin.configurationHandler().getConfigurationFile().getLeaveCommand().isEnabled())
            new LeaveCommand(this);

        if(plugin.configurationHandler().getConfigurationFile().getShopCommand().isEnabled())
            new ShopCommand(this);

        if(plugin.configurationHandler().getConfigurationFile().getLeaderboardCommand().isEnabled())
            new LeaderboardCommand(this);

        if(plugin.configurationHandler().getConfigurationFile().getScoreboardCommand().isEnabled())
            new ScoreboardCommand(this);

        if(plugin.configurationHandler().getConfigurationFile().getSetBackCommand().isEnabled())
            new SetBackCommand(this);
    }

    public Bridger plugin(){
        return plugin;
    }
}

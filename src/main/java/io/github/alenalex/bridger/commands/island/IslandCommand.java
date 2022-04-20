package io.github.alenalex.bridger.commands.island;

import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.annotation.Async;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Default;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import io.github.alenalex.bridger.abstracts.AbstractCommand;
import io.github.alenalex.bridger.manager.CommandManager;
import io.github.alenalex.bridger.models.Island;
import io.github.alenalex.bridger.models.player.UserData;
import io.github.alenalex.bridger.utils.adventure.MessageFormatter;
import io.github.alenalex.bridger.variables.LangConfigurationPaths;
import io.github.alenalex.bridger.variables.Permissions;
import io.github.alenalex.bridger.variables.PluginResponses;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

@Command(value = "island", alias = {"is","isl","bridge"})
public class IslandCommand extends AbstractCommand {

    private static final HashMap<String, String> COMMAND_DESCRIPTION = new HashMap<String, String>(){{
        put("island", "Join any free island available.");
        put("help", "Shows this help menu.");
        put("join [name]", "Joins an island with the provided name if its available!");
        put("gui", "Opens up Island Gui");
    }};

    public IslandCommand(CommandManager manager) {
        super(manager);
    }

    @Override
    public Map<String, String> getCommandDescriptionMap() {
        return COMMAND_DESCRIPTION;
    }

    @Default
    public void onDefaultCommand(Player player){
        if(player == null){
            return;
        }

        manager.plugin().gameHandler().toIsland(player).ifPresent(new Consumer<Island>() {
            @Override
            public void accept(Island island) {
                manager.plugin().getLogger().info("Player has been loaded on to the island "+island.getIslandName());
            }
        });
    }

    @SubCommand("help")
    @Async
    public void onHelpCommand(Player player){
        if(player == null){
            return;
        }

        sendHelpMessage(player);
    }

    @SubCommand("join")
    @Permission(Permissions.Commands.Island.BY_NAME)
    public void onJoinByNameCommand(@NotNull Player player, String islandName){
        final UserData userData = manager.plugin().gameHandler().userManager().of(player.getUniqueId());
        if(userData == null) {
            return;
        }

        if(StringUtils.isBlank(islandName)){
            manager.plugin().messagingUtils().sendTo(player, userData.userSettings().getLanguage().asComponent(LangConfigurationPaths.NOT_PROVIDED_VALID_ISLAND_NAME));
            return;
        }

        manager.plugin().gameHandler().toIsland(player, userData ,islandName).ifPresent(new Consumer<Island>() {
            @Override
            public void accept(Island island) {
                manager.plugin().getLogger().info("Player has been loaded on to the island "+island.getIslandName());
            }
        });
    }

    @SubCommand("gui")
    @Permission(Permissions.Commands.Island.GUI)
    public void onGuiCommand(Player player){
        manager.plugin().uiHandler().getIslandSelector().openFor(player);
    }
}

package io.github.alenalex.bridger.commands.island;

import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.annotation.Async;
import dev.triumphteam.cmd.core.annotation.Default;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import io.github.alenalex.bridger.abstracts.AbstractCommand;
import io.github.alenalex.bridger.manager.CommandManager;
import io.github.alenalex.bridger.models.BridgerIsland;
import io.github.alenalex.bridger.models.player.BridgerUserData;
import io.github.alenalex.bridger.variables.LangConfigurationPaths;
import io.github.alenalex.bridger.variables.Permissions;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;


public class IslandCommand extends AbstractCommand {

    private static final HashMap<String, String> COMMAND_DESCRIPTION = new HashMap<String, String>(){{
        put("island", "Join any free island available.");
        put("help", "Shows this help menu.");
        put("join [name]", "Joins an island with the provided name if its available!");
        put("gui", "Opens up Island Gui");
        put("leave", "Leaves your island.");
    }};

    public IslandCommand(CommandManager manager) {
        super(manager, "island", Arrays.asList("is","isl","bridge"));
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



        manager.plugin().gameHandler().toIsland(player).ifPresent(new Consumer<BridgerIsland>() {
            @Override
            public void accept(BridgerIsland bridgerIsland) {
                manager.plugin().getLogger().info("Player has been loaded on to the island "+ bridgerIsland.getIslandName());
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
        final BridgerUserData bridgerUserData = manager.plugin().gameHandler().userManager().of(player.getUniqueId());
        if(bridgerUserData == null) {
            return;
        }

        if(StringUtils.isBlank(islandName)){
            manager.plugin().messagingUtils().sendTo(player, bridgerUserData.userSettings().getLanguage().asComponent(LangConfigurationPaths.NOT_PROVIDED_VALID_ISLAND_NAME));
            return;
        }

        manager.plugin().gameHandler().toIsland(player, bridgerUserData,islandName).ifPresent(new Consumer<BridgerIsland>() {
            @Override
            public void accept(BridgerIsland bridgerIsland) {
                manager.plugin().getLogger().info("Player has been loaded on to the island "+ bridgerIsland.getIslandName());
            }
        });
    }

    @SubCommand("gui")
    @Permission(Permissions.Commands.Island.GUI)
    public void onGuiCommand(Player player){
        manager.plugin().uiHandler().getIslandSelector().openFor(player);
    }

    @SubCommand(value = "leave", alias = {"exit", "quit"})
    @Async
    public void onLeaveCommand(Player player){
        final BridgerUserData bridgerUserData = manager.plugin().gameHandler().userManager().of(player.getUniqueId());
        if(bridgerUserData == null) {
            return;
        }

        if(!manager.plugin().gameHandler().isPlayerPlaying(player))
            return;

        BridgerIsland bridgerIsland = manager.plugin().gameHandler().getIslandOfPlayer(player).orElse(null);
        if(bridgerIsland == null){
            return;
        }
        manager.plugin().gameHandler().playerQuitGame(player, bridgerUserData);
    }
}

package io.github.alenalex.bridger.commands.player;

import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.annotation.Default;
import dev.triumphteam.cmd.core.annotation.Optional;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import io.github.alenalex.bridger.abstracts.AbstractCommand;
import io.github.alenalex.bridger.manager.CommandManager;
import io.github.alenalex.bridger.models.player.UserData;
import io.github.alenalex.bridger.utils.adventure.internal.MessagePlaceholder;
import io.github.alenalex.bridger.variables.LangConfigurationPaths;
import io.github.alenalex.bridger.variables.Permissions;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class SetBackCommand extends AbstractCommand {
    public SetBackCommand(CommandManager manager) {
        super(manager, "setback", Arrays.asList("sb"));
        this.registerHelpMessage("set [value]" , "Sets the setback to provided value");
        this.registerHelpMessage("add [value]", "Adds the current value to setback");
        this.registerHelpMessage("decrease [value]", "Decrease the current value to setback");
        this.registerHelpMessage("reset", "Turn off the setback!");
        this.registerHelpMessage("off", "Turn off the setback");
    }

    @Default
    public void onDefaultCommand(@NotNull final Player player){
        sendHelpMessage(player);
    }

    @SubCommand("set")
    @Permission(Permissions.SetBack.SET_BACK)
    public void onSetCommand(@NotNull final Player player, Integer value){
        final UserData userData = manager.plugin().gameHandler().userManager().of(player.getUniqueId());
        if(userData == null)
            return;

        if(value == null){
            manager.plugin().messagingUtils().sendTo(player, userData.userSettings().getLanguage().asComponent(LangConfigurationPaths.COMMAND_WRONG_USAGE));
            return;
        }

        userData.userSettings().setSetBack(value);
        manager.plugin().messagingUtils().sendTo(player, userData.userSettings().getLanguage().asComponent(LangConfigurationPaths.SETBACK_SET,
                MessagePlaceholder.of("%value%", userData.userSettings().getSetBack()),
                MessagePlaceholder.of("%name%", player.getName())
        ));
    }

    @SubCommand("add")
    public void onAddCommand(@NotNull final Player player, @Optional Integer value){
        final UserData userData = manager.plugin().gameHandler().userManager().of(player.getUniqueId());
        if(userData == null)
            return;

        if(value == null){
            userData.userSettings().incrementSetBack();
        }else {
            userData.userSettings().incrementSetBackBy(value);
        }
        manager.plugin().messagingUtils().sendTo(player, userData.userSettings().getLanguage().asComponent(LangConfigurationPaths.SETBACK_SET,
                MessagePlaceholder.of("%value%", userData.userSettings().getSetBack()),
                MessagePlaceholder.of("%name%", player.getName())
        ));
    }

    @SubCommand("decrease")
    public void onDecreaseCommand(@NotNull final Player player, @Optional Integer value){
        final UserData userData = manager.plugin().gameHandler().userManager().of(player.getUniqueId());
        if(userData == null)
            return;

        if(value == null){
            userData.userSettings().decrementSetBack();
        }else {
            userData.userSettings().decrementSetBackBy(value);
        }
        manager.plugin().messagingUtils().sendTo(player, userData.userSettings().getLanguage().asComponent(LangConfigurationPaths.SETBACK_SET,
                MessagePlaceholder.of("%value%", userData.userSettings().getSetBack()),
                MessagePlaceholder.of("%name%", player.getName())
        ));
    }

    @SubCommand(value = "reset", alias = {"off"})
    public void onResetCommand(@NotNull final Player player){
        final UserData userData = manager.plugin().gameHandler().userManager().of(player.getUniqueId());
        if(userData == null)
            return;

        userData.userSettings().resetSetBack();
        manager.plugin().messagingUtils().sendTo(player, userData.userSettings().getLanguage().asComponent(LangConfigurationPaths.SETBACK_REMOVED));
    }


}

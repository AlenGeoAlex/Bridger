package io.github.alenalex.bridger.commands.setup;

import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.annotation.*;
import io.github.alenalex.bridger.abstracts.AbstractCommand;
import io.github.alenalex.bridger.manager.CommandManager;
import io.github.alenalex.bridger.models.setup.SetupSession;
import io.github.alenalex.bridger.utils.adventure.internal.MessagePlaceholder;
import io.github.alenalex.bridger.variables.Permissions;
import io.github.alenalex.bridger.variables.PluginResponses;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

import java.util.Map;

@Command(value = "session")
public class SessionCommand extends AbstractCommand {

    private static final HashMap<String, String> COMMAND_DESCRIPTION = new HashMap<String, String>(){{
        put("create [name]", "Creates a session with the name.");
        put("delete", "Deletes any active session on your behalf");
        put("validate", "Validates your current session, It will show if you have missed any compulsory configuration");
        put("pos1", "Sets the position 1 to your current location");
        put("pos2", "Sets the position 2 to your current location");
        put("spawn", "Sets the spawn position of the player to your current location");
        put("end", "Sets the end position of the player to your current location");
        put("perm [perm.node]", "Sets the perm node of the island that a player required to join");
        put("min-blocks [min.blocks]", "Sets the minimum no of blocks a player required to place in order to complete the practice");
        put("min-time [min.time]", "Sets the minimum time a player required to complete the practice");
        put("setjoincost [cost]", "Sets the cost needed to join the island" );
        put("setreward [cost]", "Sets the amount of coins if a player wins a round!");
    }};

    public SessionCommand(CommandManager manager) {
        super(manager);
    }

    @Override
    public Map<String, String> getCommandDescriptionMap() {
        return COMMAND_DESCRIPTION;
    }

    @Default
    @Permission(Permissions.Commands.Sessions.DEFAULT)
    public void onDefaultCommand(@NotNull Player player){
        sendHelpMessage(player);
    }

    @SubCommand("create")
    @Async
    @Permission(Permissions.Commands.Sessions.DEFAULT)
    public void onCreateCommand(@NotNull Player player, @Nullable String sessionName){
        if(StringUtils.isBlank(sessionName)){
            manager.plugin().messagingUtils().sendTo(player, PluginResponses.Commands.Sessions.NO_SESSION_NAME_PROVIDED);
            return;
        }

        manager.plugin().setupSessionManager().createNewSession(player, sessionName);
    }

    @SubCommand("delete")
    @Async
    @Permission(Permissions.Commands.Sessions.DEFAULT)
    public void onDeleteCommand(@NotNull Player player){
        manager.plugin().setupSessionManager().removeSession(player);
    }

    @SubCommand("pos1")
    @Async
    @Permission(Permissions.Commands.Sessions.DEFAULT)
    public void onSetPos1Command(@NotNull Player player){
        final SetupSession setupSession = manager.plugin().setupSessionManager().of(player.getUniqueId());

        if(setupSession == null){
            manager.plugin().messagingUtils().sendTo(player, PluginResponses.Commands.Sessions.DOES_NOT_HAVE_AN_ACTIVE_SESSION);
            return;
        }

        setupSession.setPos1(player.getLocation());
        manager.plugin().messagingUtils().sendTo(player,
                PluginResponses.Commands.Sessions.SUCCESSFULLY_SET_LOCATION
                , MessagePlaceholder.of("%key%", "position-1")
                );
    }

    @SubCommand("pos2")
    @Async
    @Permission(Permissions.Commands.Sessions.DEFAULT)
    public void onSetPos2Command(@NotNull Player player){
        final SetupSession setupSession = manager.plugin().setupSessionManager().of(player.getUniqueId());

        if(setupSession == null){
            manager.plugin().messagingUtils().sendTo(player, PluginResponses.Commands.Sessions.DOES_NOT_HAVE_AN_ACTIVE_SESSION);
            return;
        }

        setupSession.setPos2(player.getLocation());
        manager.plugin().messagingUtils().sendTo(player,
                PluginResponses.Commands.Sessions.SUCCESSFULLY_SET_LOCATION
                , MessagePlaceholder.of("%key%", "position-2")
        );
    }

    @SubCommand("spawn")
    @Async
    @Permission(Permissions.Commands.Sessions.DEFAULT)
    public void onSetSpawnCommand(@NotNull Player player){
        final SetupSession setupSession = manager.plugin().setupSessionManager().of(player.getUniqueId());

        if(setupSession == null){
            manager.plugin().messagingUtils().sendTo(player, PluginResponses.Commands.Sessions.DOES_NOT_HAVE_AN_ACTIVE_SESSION);
            return;
        }

        setupSession.setSpawnPoint(player.getLocation());
        manager.plugin().messagingUtils().sendTo(player,
                PluginResponses.Commands.Sessions.SUCCESSFULLY_SET_LOCATION
                , MessagePlaceholder.of("%key%", "spawn-point")
        );
    }

    @SubCommand("end")
    @Async
    @Permission(Permissions.Commands.Sessions.DEFAULT)
    public void onSetEndCommand(@NotNull Player player){
        final SetupSession setupSession = manager.plugin().setupSessionManager().of(player.getUniqueId());

        if(setupSession == null){
            manager.plugin().messagingUtils().sendTo(player, PluginResponses.Commands.Sessions.DOES_NOT_HAVE_AN_ACTIVE_SESSION);
            return;
        }

        setupSession.setEndPoint(player.getLocation());
        manager.plugin().messagingUtils().sendTo(player,
                PluginResponses.Commands.Sessions.SUCCESSFULLY_SET_LOCATION
                , MessagePlaceholder.of("%key%", "end-point")
        );
    }

    @SubCommand("min-blocks")
    @Async
    @Permission(Permissions.Commands.Sessions.DEFAULT)
    public void onSetMinBlocks(@NotNull Player player, Integer integer){
        if(integer == null) {
            manager.plugin().messagingUtils().sendTo(player, PluginResponses.Commands.Sessions.NOT_PROVIDED_VALID_VALUE);
            return;
        }

        final SetupSession setupSession = manager.plugin().setupSessionManager().of(player.getUniqueId());

        if(setupSession == null){
            manager.plugin().messagingUtils().sendTo(player, PluginResponses.Commands.Sessions.DOES_NOT_HAVE_AN_ACTIVE_SESSION);
            return;
        }

        setupSession.setMinBlocksRequired(integer);
        manager.plugin().messagingUtils().sendTo(player,
                PluginResponses.Commands.Sessions.SUCCESSFULLY_SET_VALUE
                , MessagePlaceholder.of("%key%", "min-blocks")
                , MessagePlaceholder.of("%value%", integer)
        );
    }

    @SubCommand("min-time")
    @Async
    @Permission(Permissions.Commands.Sessions.DEFAULT)
    public void onSetMinTime(@NotNull Player player, Integer integer){
        if(integer == null) {
            manager.plugin().messagingUtils().sendTo(player, PluginResponses.Commands.Sessions.NOT_PROVIDED_VALID_VALUE);
            return;
        }

        final SetupSession setupSession = manager.plugin().setupSessionManager().of(player.getUniqueId());

        if(setupSession == null){
            manager.plugin().messagingUtils().sendTo(player, PluginResponses.Commands.Sessions.DOES_NOT_HAVE_AN_ACTIVE_SESSION);
            return;
        }

        setupSession.setMinSecRequired(integer);
        manager.plugin().messagingUtils().sendTo(player,
                PluginResponses.Commands.Sessions.SUCCESSFULLY_SET_VALUE
                , MessagePlaceholder.of("%key%", "min-time")
                , MessagePlaceholder.of("%value%", integer)
        );
    }

    @SubCommand("perm")
    @Async
    @Suggestion("bridger.join.")
    @Permission(Permissions.Commands.Sessions.DEFAULT)
    public void onSetPerm(@NotNull Player player, String perm){
        if(StringUtils.isBlank(perm)) {
            manager.plugin().messagingUtils().sendTo(player, PluginResponses.Commands.Sessions.NOT_PROVIDED_VALID_VALUE);
            return;
        }

        final SetupSession setupSession = manager.plugin().setupSessionManager().of(player.getUniqueId());

        if(setupSession == null){
            manager.plugin().messagingUtils().sendTo(player, PluginResponses.Commands.Sessions.DOES_NOT_HAVE_AN_ACTIVE_SESSION);
            return;
        }

        setupSession.setPermissionRequired(perm);
        manager.plugin().messagingUtils().sendTo(player,
                PluginResponses.Commands.Sessions.SUCCESSFULLY_SET_VALUE
                , MessagePlaceholder.of("%key%", "permission required")
                , MessagePlaceholder.of("%value%", perm)
        );
    }

    //TODO Validate Command
    @SubCommand("validate")
    @Async
    @Permission(Permissions.Commands.Sessions.DEFAULT)
    public void onValidateCommand(@NotNull Player player){
        final SetupSession setupSession = manager.plugin().setupSessionManager().of(player.getUniqueId());

        if(setupSession == null){
            manager.plugin().messagingUtils().sendTo(player, PluginResponses.Commands.Sessions.DOES_NOT_HAVE_AN_ACTIVE_SESSION);
            return;
        }

        SetupSession.ValidityStatus valid = setupSession.isValid();
        if(valid == SetupSession.ValidityStatus.VALID){
            manager.plugin().setupSessionManager().completeSession(player);
        }else {
            final String reason = WordUtils.capitalize(valid.name().replace("_", " "));
            manager.plugin().messagingUtils().sendTo(player, PluginResponses.Commands.Sessions.FAILED_VALIDITY
                ,MessagePlaceholder.of("%reason%",reason)
            );
            return;
        }
    }

    @SubCommand("setjoincost")
    @Async
    public void onSetJoinCostCommand(@NotNull Player player, Double cost){
        if(cost == null){
            manager.plugin().messagingUtils().sendTo(player, PluginResponses.Commands.Sessions.NOT_PROVIDED_VALID_VALUE);
            return;
        }

        final SetupSession setupSession = manager.plugin().setupSessionManager().of(player.getUniqueId());

        if(setupSession == null){
            manager.plugin().messagingUtils().sendTo(player, PluginResponses.Commands.Sessions.DOES_NOT_HAVE_AN_ACTIVE_SESSION);
            return;
        }

        setupSession.setJoinCost(cost);
        manager.plugin().messagingUtils().sendTo(player,
                PluginResponses.Commands.Sessions.SUCCESSFULLY_SET_VALUE
                , MessagePlaceholder.of("%key%", "join cost")
                , MessagePlaceholder.of("%value%", cost)
        );
    }

    @SubCommand("setreward")
    @Async
    public void onSetReward(@NotNull Player player, Double cost){
        if(cost == null){
            manager.plugin().messagingUtils().sendTo(player, PluginResponses.Commands.Sessions.NOT_PROVIDED_VALID_VALUE);
            return;
        }

        final SetupSession setupSession = manager.plugin().setupSessionManager().of(player.getUniqueId());

        if(setupSession == null){
            manager.plugin().messagingUtils().sendTo(player, PluginResponses.Commands.Sessions.DOES_NOT_HAVE_AN_ACTIVE_SESSION);
            return;
        }

        setupSession.setRewardCost(cost);
        manager.plugin().messagingUtils().sendTo(player,
                PluginResponses.Commands.Sessions.SUCCESSFULLY_SET_VALUE
                , MessagePlaceholder.of("%key%", "reward coins")
                , MessagePlaceholder.of("%value%", cost)
        );
    }


}

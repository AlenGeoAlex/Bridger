package io.github.alenalex.bridger.manager;

import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.abstracts.AbstractRegistry;
import io.github.alenalex.bridger.models.Island;
import io.github.alenalex.bridger.models.setup.SetupSession;
import io.github.alenalex.bridger.utils.adventure.internal.MessagePlaceholder;
import io.github.alenalex.bridger.variables.PluginResponses;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class SetupSessionManager extends AbstractRegistry<UUID, SetupSession> {

    private final List<UUID> sessionRemoveConfirmList;

    public SetupSessionManager(Bridger plugin) {
        super(plugin);
        this.sessionRemoveConfirmList = new ArrayList<>();
    }

    public boolean isSessionWithNameActive(@NotNull String sessionName){
        return getValueStream()
                .anyMatch(session -> session.getIslandName().equals(sessionName));
    }

    public void createNewSession(@NotNull Player player, @NotNull String sessionName){
        if(plugin.gameHandler().islandManager().isKeyRegistered(sessionName)){
            plugin.messagingUtils().sendTo(player, PluginResponses.SetupSession.ISLAND_NAME_EXISTS);
            return;
        }

        if(isSessionWithNameActive(sessionName)){
            plugin.messagingUtils().sendTo(player, PluginResponses.SetupSession.SESSION_WITH_NAME_EXISTS);
            return;
        }

        if(isKeyRegistered(player.getUniqueId())){
            plugin.messagingUtils().sendTo(player, PluginResponses.SetupSession.USER_ALREADY_HAS_AN_ACTIVE_SESSION);
            return;
        }

        final SetupSession setupSession = new SetupSession(player.getUniqueId(), sessionName);
        register(setupSession.getPlayerUID(), setupSession);
        plugin.messagingUtils().sendTo(player, PluginResponses.SetupSession.CREATED_SETUP_SESSION, MessagePlaceholder.of("%session-name%", sessionName));
    }

    public void removeSession(@NotNull Player player){
        if(sessionRemoveConfirmList.contains(player.getUniqueId())){
            String sessionName = pop(player.getUniqueId()).value().getIslandName();
            plugin.messagingUtils().sendTo(player, PluginResponses.SetupSession.PURGED_SESSION, MessagePlaceholder.of("%session-name%", sessionName));
            sessionRemoveConfirmList.remove(player.getUniqueId());
            return;
        }

        if(!isKeyRegistered(player.getUniqueId())){
            plugin.messagingUtils().sendTo(player, PluginResponses.SetupSession.USER_DOES_NOT_HAVE_AN_ACTIVE_SESSION);
            return;
        }

        sessionRemoveConfirmList.add(player.getUniqueId());
        plugin.messagingUtils().sendTo(player, PluginResponses.SetupSession.PURGE_CONFIRM);
    }

    public void completeSession(@NotNull Player player){
        if(!isKeyRegistered(player.getUniqueId()))
            return;

        final SetupSession setupSession = of(player.getUniqueId());
        final Island island = setupSession.asIsland();
        final Map<String, Object> islandData = setupSession.asSerializedSession();

        plugin.gameHandler().islandManager().registerIsland(island);
        plugin.configurationHandler().getIslandConfiguration().setIslandData(island.getIslandName(), islandData);
        plugin.messagingUtils().sendTo(player, PluginResponses.SetupSession.SUCCESS
            ,MessagePlaceholder.of("%name%", island.getIslandName())
        );
        remove(player.getUniqueId());
    }

}

package io.github.alenalex.bridger.handler;

import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.manager.IslandManager;
import io.github.alenalex.bridger.manager.UserManager;
import io.github.alenalex.bridger.models.Island;
import io.github.alenalex.bridger.models.player.UserData;
import io.github.alenalex.bridger.utils.adventure.internal.MessagePlaceholder;
import io.github.alenalex.bridger.variables.LangConfigurationPaths;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class GameHandler {

    private final Bridger plugin;
    private final IslandManager islandManager;
    private final UserManager userManager;

    private final ConcurrentHashMap<UUID, String> activeBridges;

    public GameHandler(Bridger plugin) {
        this.plugin = plugin;
        this.islandManager = new IslandManager(plugin);
        this.userManager = new UserManager(plugin);
        this.activeBridges = new ConcurrentHashMap<>();
    }

    public IslandManager islandManager(){
        return islandManager;
    }

    public UserManager userManager(){
        return userManager;
    }

    public Bridger getPlugin() {
        return plugin;
    }

    public boolean isPlayerPlaying(@NotNull Player player){
        return activeBridges.containsKey(player.getUniqueId());
    }

    public boolean isPlayerPlaying(@NotNull UUID pUID){
        return activeBridges.containsKey(pUID);
    }

    public Optional<Island> getIslandOfPlayer(@NotNull Player player){
        return Optional.ofNullable(islandManager.of(activeBridges.get(player.getUniqueId())));
    }

    public Optional<Island> getIslandOfPlayer(@NotNull UUID pUID){
        return Optional.ofNullable(islandManager.of(activeBridges.get(pUID)));
    }

    public Optional<Player> getPlayerOfIsland(@NotNull String islandName){
        return activeBridges
                .values()
                .stream()
                .filter(islandName::equals)
                .findAny()
                .map(Bukkit::getPlayer);
    }

    public Optional<Island> toIsland(@NotNull Player player, @NotNull UserData userData, String islandName){

        if(activeBridges.containsKey(player.getUniqueId())){
            plugin.messagingUtils().sendTo(player, userData.userSettings().getLanguage().asComponent(LangConfigurationPaths.ALREADY_HAVE_ISLAND));
            return Optional.empty();
        }

        Island island = islandManager.getFreeIslandByName(player, islandName).orElse(null);

        if(island == null){
            plugin.messagingUtils().sendTo(player, userData.userSettings().getLanguage().asComponent(LangConfigurationPaths.NO_FREE_ISLANDS));
            return Optional.empty();
        }

        island.setOccupied();

        activeBridges.put(player.getUniqueId(), island.getIslandName());
        userData.userMatchCache().setPlayerAsIdle();
        island.teleportToSpawn(player);
        plugin.messagingUtils().sendTo(player,
                userData.userSettings().getLanguage().asComponent(LangConfigurationPaths.TELEPORTED_TO_ISLAND,
                        MessagePlaceholder.of("%island-name%", island.getIslandName())
                ));
        return Optional.of(island);
    }

    public Optional<Island> toIsland(@NotNull Player player){
        final UserData userData = userManager.of(player.getUniqueId());
        if(userData == null) {
            plugin.getLogger().severe("Failed to get the data for user @"+getClass().getSimpleName()+"#toIsland(Player)");
            return Optional.empty();
        }

        if(activeBridges.containsKey(player.getUniqueId())){
            plugin.messagingUtils().sendTo(player, userData.userSettings().getLanguage().asComponent(LangConfigurationPaths.ALREADY_HAVE_ISLAND));
            return Optional.empty();
        }

        Island island = islandManager.getAnyFreeIsland(player).orElse(null);

        if(island == null){
            plugin.messagingUtils().sendTo(player, userData.userSettings().getLanguage().asComponent(LangConfigurationPaths.NO_FREE_ISLANDS));
            return Optional.empty();
        }

        island.setOccupied();

        activeBridges.put(player.getUniqueId(), island.getIslandName());
        userData.userMatchCache().setPlayerAsIdle();
        island.teleportToSpawn(player);
        plugin.messagingUtils().sendTo(player,
                userData.userSettings().getLanguage().asComponent(LangConfigurationPaths.TELEPORTED_TO_ISLAND,
                MessagePlaceholder.of("%island-name%", island.getIslandName())
                ));
        return Optional.of(island);
    }

    public void toIsland(@NotNull Player player, @NotNull UserData userData, @NotNull Island island){
        if(activeBridges.containsKey(player.getUniqueId())){
            plugin.messagingUtils().sendTo(player, userData.userSettings().getLanguage().asComponent(LangConfigurationPaths.ALREADY_HAVE_ISLAND));
            return;
        }

        island.setOccupied();

        activeBridges.put(player.getUniqueId(), island.getIslandName());
        userData.userMatchCache().setPlayerAsIdle();
        island.teleportToSpawn(player);
        plugin.messagingUtils().sendTo(player,
                userData.userSettings().getLanguage().asComponent(LangConfigurationPaths.TELEPORTED_TO_ISLAND,
                        MessagePlaceholder.of("%island-name%", island.getIslandName())
                ));
    }

    public void playerFirstBlock(@NotNull UserData userData){
        userData.userMatchCache().setPlayerAsPlaying();
    }

    private void playerRestartGame(@NotNull Player player){
        if(!activeBridges.containsKey(player.getUniqueId()))
            return;

        final UserData userData = userManager.of(player.getUniqueId());
        if(userData == null) {
            plugin.getLogger().severe("Failed to get the data for user @"+getClass().getSimpleName()+"#playerRestartGame(Player)");
            return;
        }

        final Island island = islandManager.of(activeBridges.get(player.getUniqueId()));
        island.teleportToSpawn(player);
        UserManager.setIslandItemsOnPlayer(player);
        island.setResetting();
        userData.userMatchCache().resetPlacedBlocks();
        island.setIdle();
        userData.userMatchCache().setPlayerAsIdle();
    }

    public void playerCompleteGame(@NotNull Player player, long completeTime){
        if(!activeBridges.containsKey(player.getUniqueId()))
            return;

        final UserData userData = userManager.of(player.getUniqueId());
        if(userData == null) {
            plugin.getLogger().severe("Failed to get the data for user @"+getClass().getSimpleName()+"#playerRestartGame(Player)");
            return;
        }

        final Island island = islandManager.of(activeBridges.get(player.getUniqueId()));
        userData.userStats().addAsCompletedGame();
        userData.userMatchCache().setCurrentTime(completeTime - userData.userMatchCache().getStartTime());

        if(userData.userMatchCache().getCurrentTime() < userData.userStats().getBestTime()){
            final String oldBestTime = userData.userStats().getBestTimeAsString();
            userData.userStats().setBestTime(userData.userMatchCache().getCurrentTime());
            final String newBestTime = userData.userStats().getBestTimeAsString();
            userData.userSettings().getLanguage().asComponent(LangConfigurationPaths.NEW_BEST_TIME_TO_PLAYER,
                    MessagePlaceholder.of("%time%",userData.userStats().getBestTime())
            );

            if(plugin.configurationHandler().getConfigurationFile().isBroadcastNewBestTimeToAllPlayersEnabled()){
                new BukkitRunnable() {
                    @Override
                    public void run() {

                        for(UserData serverPlayer : userManager.getValueCollection()){
                            if(serverPlayer.getPlayerUID().equals(userData.getPlayerUID()))
                                continue;

                            plugin.messagingUtils().sendTo(player,
                                    serverPlayer.userSettings().getLanguage().asComponent(LangConfigurationPaths.NEW_BEST_TIME_TO_BROADCAST,
                                            MessagePlaceholder.of("%old-best%", oldBestTime),
                                            MessagePlaceholder.of("%new-best%", newBestTime),
                                            MessagePlaceholder.of("%name%", player.getName())
                                    )
                            );
                        }
                    }
                }.runTaskAsynchronously(plugin);
            }
        }
        if(island.getRewards() > 0){
            plugin.pluginHookManager().getEconomyProvider().deposit(player, island.getRewards());
        }
        plugin.messagingUtils().sendTo(player,
                userData.userSettings().getLanguage().asComponent(LangConfigurationPaths.COMPLETED_GAME,
                        MessagePlaceholder.of("%time%", userData.userMatchCache().getCurrentTimeAsString())
                        )
                );
        playerRestartGame(player);
    }

    public void playerFailedGame(@NotNull Player player){
        if(!activeBridges.containsKey(player.getUniqueId()))
            return;

        final UserData userData = userManager.of(player.getUniqueId());
        if(userData == null) {
            plugin.getLogger().severe("Failed to get the data for user @"+getClass().getSimpleName()+"#playerRestartGame(Player)");
            return;
        }

        final Island island = islandManager.of(activeBridges.get(player.getUniqueId()));
        userData.userStats().addGame();
        playerRestartGame(player);;
    }

    public void playerQuitGame(Player player, UserData userData){
        if(!activeBridges.containsKey(player.getUniqueId()))
            return;

        UserManager.handleLobbyTransport(player);

        final Island island = islandManager.of(activeBridges.get(player.getUniqueId()));
        islandManager.removeSpectators(island);
        removePlayerFromIsland(userData, island);
        plugin.messagingUtils().sendTo(player, userData.userSettings().getLanguage().asComponent(LangConfigurationPaths.PLAYER_LEAVE_GAME));
    }

    public void onPlayerQuit(Player player){
        if(!activeBridges.containsKey(player.getUniqueId()))
            return;

        final UserData userData = userManager.of(player.getUniqueId());
        if(userData == null) {
            plugin.getLogger().severe("Failed to get the data for user @"+getClass().getSimpleName()+"#playerQuitServer(Player)");
            return;
        }

        final Island island = islandManager.of(activeBridges.get(player.getUniqueId()));
        islandManager.removeSpectators(island);
        island.setResetting();
        userData.userMatchCache().forceResetPlacedBlocks();
        island.setIdle();
        activeBridges.remove(player.getUniqueId());
    }

    public void kickPlayerFromIsland(@NotNull Player player){
        if(!activeBridges.containsKey(player.getUniqueId()))
            return;

        final UserData userData = userManager.of(player.getUniqueId());
        if(userData == null) {
            plugin.getLogger().severe("Failed to get the data for user @"+getClass().getSimpleName()+"#kickPlayerFromIsland(Player)");
            return;
        }
        UserManager.handleLobbyTransport(player);

        final Island island = islandManager.of(activeBridges.get(player.getUniqueId()));
        removePlayerFromIsland(userData, island);
        //TODO

    }

    private void removePlayerFromIsland(@NotNull UserData userData, @NotNull Island island){
        island.setResetting();
        userData.userMatchCache().resetPlacedBlocks();
        userData.userMatchCache().setPlayerAsLobby();
        activeBridges.remove(userData.getPlayerUID());
        island.setIdle();
    }

    public List<String> getActivePlayerNames(){
        return activeBridges
                .keySet()
                .stream()
                .map(uuid -> Bukkit.getPlayer(uuid))
                .filter(Objects::nonNull)
                .map(player -> player.getName())
                .collect(Collectors.toList());
    }

    public String asJson(){
        return Bridger.gsonInstance().toJson(activeBridges);
    }

    public ConcurrentHashMap<UUID, String> getActiveBridges() {
        return activeBridges;
    }
}

package io.github.alenalex.bridger.handler;

import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.manager.IslandManagerImpl;
import io.github.alenalex.bridger.manager.UserManagerImpl;
import io.github.alenalex.bridger.models.BridgerIsland;
import io.github.alenalex.bridger.models.player.BridgerUserData;
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
    private final IslandManagerImpl islandManagerImpl;
    private final UserManagerImpl userManagerImpl;

    private final ConcurrentHashMap<UUID, String> activeBridges;

    public GameHandler(Bridger plugin) {
        this.plugin = plugin;
        this.islandManagerImpl = new IslandManagerImpl(plugin);
        this.userManagerImpl = new UserManagerImpl(plugin);
        this.activeBridges = new ConcurrentHashMap<>();
    }

    public IslandManagerImpl islandManager(){
        return islandManagerImpl;
    }

    public UserManagerImpl userManager(){
        return userManagerImpl;
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

    public Optional<BridgerIsland> getIslandOfPlayer(@NotNull Player player){
        return Optional.ofNullable(islandManagerImpl.of(activeBridges.get(player.getUniqueId())));
    }

    public Optional<BridgerIsland> getIslandOfPlayer(@NotNull UUID pUID){
        return Optional.ofNullable(islandManagerImpl.of(activeBridges.get(pUID)));
    }

    public Optional<Player> getPlayerOfIsland(@NotNull String islandName){
        return activeBridges
                .values()
                .stream()
                .filter(islandName::equals)
                .findAny()
                .map(Bukkit::getPlayer);
    }

    public Optional<BridgerIsland> toIsland(@NotNull Player player, @NotNull BridgerUserData bridgerUserData, String islandName){

        if(activeBridges.containsKey(player.getUniqueId())){
            plugin.messagingUtils().sendTo(player, bridgerUserData.userSettings().getLanguage().asComponent(LangConfigurationPaths.ALREADY_HAVE_ISLAND));
            return Optional.empty();
        }

        BridgerIsland bridgerIsland = (BridgerIsland) islandManagerImpl.getFreeIslandByName(player, islandName).orElse(null);

        if(bridgerIsland == null){
            plugin.messagingUtils().sendTo(player, bridgerUserData.userSettings().getLanguage().asComponent(LangConfigurationPaths.NO_FREE_ISLANDS));
            return Optional.empty();
        }

        bridgerIsland.setOccupied();

        activeBridges.put(player.getUniqueId(), bridgerIsland.getIslandName());
        bridgerUserData.userMatchCache().setPlayerAsIdle();
        bridgerIsland.teleportToSpawn(player);
        plugin.messagingUtils().sendTo(player,
                bridgerUserData.userSettings().getLanguage().asComponent(LangConfigurationPaths.TELEPORTED_TO_ISLAND,
                        MessagePlaceholder.of("%island-name%", bridgerIsland.getIslandName())
                ));
        return Optional.of(bridgerIsland);
    }

    public Optional<BridgerIsland> toIsland(@NotNull Player player){
        final BridgerUserData bridgerUserData = userManagerImpl.of(player.getUniqueId());
        if(bridgerUserData == null) {
            plugin.getLogger().severe("Failed to get the data for user @"+getClass().getSimpleName()+"#toIsland(Player)");
            return Optional.empty();
        }

        if(activeBridges.containsKey(player.getUniqueId())){
            plugin.messagingUtils().sendTo(player, bridgerUserData.userSettings().getLanguage().asComponent(LangConfigurationPaths.ALREADY_HAVE_ISLAND));
            return Optional.empty();
        }

        BridgerIsland bridgerIsland = (BridgerIsland) islandManagerImpl.getAnyFreeIsland(player).orElse(null);

        if(bridgerIsland == null){
            plugin.messagingUtils().sendTo(player, bridgerUserData.userSettings().getLanguage().asComponent(LangConfigurationPaths.NO_FREE_ISLANDS));
            return Optional.empty();
        }

        bridgerIsland.setOccupied();

        activeBridges.put(player.getUniqueId(), bridgerIsland.getIslandName());
        bridgerUserData.userMatchCache().setPlayerAsIdle();
        bridgerIsland.teleportToSpawn(player);
        plugin.messagingUtils().sendTo(player,
                bridgerUserData.userSettings().getLanguage().asComponent(LangConfigurationPaths.TELEPORTED_TO_ISLAND,
                MessagePlaceholder.of("%island-name%", bridgerIsland.getIslandName())
                ));
        return Optional.of(bridgerIsland);
    }

    public void toIsland(@NotNull Player player, @NotNull BridgerUserData bridgerUserData, @NotNull BridgerIsland bridgerIsland){
        if(activeBridges.containsKey(player.getUniqueId())){
            plugin.messagingUtils().sendTo(player, bridgerUserData.userSettings().getLanguage().asComponent(LangConfigurationPaths.ALREADY_HAVE_ISLAND));
            return;
        }

        bridgerIsland.setOccupied();

        activeBridges.put(player.getUniqueId(), bridgerIsland.getIslandName());
        bridgerUserData.userMatchCache().setPlayerAsIdle();
        bridgerIsland.teleportToSpawn(player);
        plugin.messagingUtils().sendTo(player,
                bridgerUserData.userSettings().getLanguage().asComponent(LangConfigurationPaths.TELEPORTED_TO_ISLAND,
                        MessagePlaceholder.of("%island-name%", bridgerIsland.getIslandName())
                ));
    }

    public void playerFirstBlock(@NotNull BridgerUserData bridgerUserData){
        bridgerUserData.userMatchCache().setPlayerAsPlaying();
    }

    private void playerRestartGame(@NotNull Player player){
        if(!activeBridges.containsKey(player.getUniqueId()))
            return;

        final BridgerUserData bridgerUserData = userManagerImpl.of(player.getUniqueId());
        if(bridgerUserData == null) {
            plugin.getLogger().severe("Failed to get the data for user @"+getClass().getSimpleName()+"#playerRestartGame(Player)");
            return;
        }

        final BridgerIsland bridgerIsland = islandManagerImpl.of(activeBridges.get(player.getUniqueId()));
        bridgerIsland.teleportToSpawn(player);
        UserManagerImpl.setIslandItemsOnPlayer(player);
        bridgerIsland.setResetting();
        bridgerUserData.userMatchCache().resetPlacedBlocks();
        bridgerIsland.setIdle();
        bridgerUserData.userMatchCache().setPlayerAsIdle();
    }

    public void playerCompleteGame(@NotNull Player player, long completeTime){
        if(!activeBridges.containsKey(player.getUniqueId()))
            return;

        final BridgerUserData bridgerUserData = userManagerImpl.of(player.getUniqueId());
        if(bridgerUserData == null) {
            plugin.getLogger().severe("Failed to get the data for user @"+getClass().getSimpleName()+"#playerRestartGame(Player)");
            return;
        }

        final BridgerIsland bridgerIsland = islandManagerImpl.of(activeBridges.get(player.getUniqueId()));
        bridgerUserData.userStats().addAsCompletedGame();
        bridgerUserData.userMatchCache().setCurrentTime(completeTime - bridgerUserData.userMatchCache().getStartTime());

        if(bridgerUserData.userMatchCache().getCurrentTime() < bridgerUserData.userStats().getBestTime()){
            final String oldBestTime = bridgerUserData.userStats().getBestTimeAsString();
            bridgerUserData.userStats().setBestTime(bridgerUserData.userMatchCache().getCurrentTime());
            final String newBestTime = bridgerUserData.userStats().getBestTimeAsString();
            bridgerUserData.userSettings().getLanguage().asComponent(LangConfigurationPaths.NEW_BEST_TIME_TO_PLAYER,
                    MessagePlaceholder.of("%time%", bridgerUserData.userStats().getBestTime())
            );

            if(plugin.configurationHandler().getConfigurationFile().isBroadcastNewBestTimeToAllPlayersEnabled()){
                new BukkitRunnable() {
                    @Override
                    public void run() {

                        for(BridgerUserData serverPlayer : userManagerImpl.getValueCollection()){
                            if(serverPlayer.getPlayerUID().equals(bridgerUserData.getPlayerUID()))
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
        if(bridgerIsland.getRewards() > 0){
            plugin.pluginHookManager().getEconomyProvider().deposit(player, bridgerIsland.getRewards());
        }
        plugin.messagingUtils().sendTo(player,
                bridgerUserData.userSettings().getLanguage().asComponent(LangConfigurationPaths.COMPLETED_GAME,
                        MessagePlaceholder.of("%time%", bridgerUserData.userMatchCache().getCurrentTimeAsString())
                        )
                );
        playerRestartGame(player);
    }

    public void playerFailedGame(@NotNull Player player){
        if(!activeBridges.containsKey(player.getUniqueId()))
            return;

        final BridgerUserData bridgerUserData = userManagerImpl.of(player.getUniqueId());
        if(bridgerUserData == null) {
            plugin.getLogger().severe("Failed to get the data for user @"+getClass().getSimpleName()+"#playerRestartGame(Player)");
            return;
        }

        final BridgerIsland bridgerIsland = islandManagerImpl.of(activeBridges.get(player.getUniqueId()));
        bridgerUserData.userStats().addGame();
        playerRestartGame(player);;
    }

    public void playerQuitGame(Player player, BridgerUserData bridgerUserData){
        if(!activeBridges.containsKey(player.getUniqueId()))
            return;

        UserManagerImpl.handleLobbyTransport(player);

        final BridgerIsland bridgerIsland = islandManagerImpl.of(activeBridges.get(player.getUniqueId()));
        islandManagerImpl.removeSpectators(bridgerIsland);
        removePlayerFromIsland(bridgerUserData, bridgerIsland);
        plugin.messagingUtils().sendTo(player, bridgerUserData.userSettings().getLanguage().asComponent(LangConfigurationPaths.PLAYER_LEAVE_GAME));
    }

    public void onPlayerQuit(Player player){
        if(!activeBridges.containsKey(player.getUniqueId()))
            return;

        final BridgerUserData bridgerUserData = userManagerImpl.of(player.getUniqueId());
        if(bridgerUserData == null) {
            plugin.getLogger().severe("Failed to get the data for user @"+getClass().getSimpleName()+"#playerQuitServer(Player)");
            return;
        }

        final BridgerIsland bridgerIsland = islandManagerImpl.of(activeBridges.get(player.getUniqueId()));
        islandManagerImpl.removeSpectators(bridgerIsland);
        bridgerIsland.setResetting();
        bridgerUserData.userMatchCache().forceResetPlacedBlocks();
        bridgerIsland.setIdle();
        activeBridges.remove(player.getUniqueId());
    }

    public void kickPlayerFromIsland(@NotNull Player player){
        if(!activeBridges.containsKey(player.getUniqueId()))
            return;

        final BridgerUserData bridgerUserData = userManagerImpl.of(player.getUniqueId());
        if(bridgerUserData == null) {
            plugin.getLogger().severe("Failed to get the data for user @"+getClass().getSimpleName()+"#kickPlayerFromIsland(Player)");
            return;
        }
        UserManagerImpl.handleLobbyTransport(player);

        final BridgerIsland bridgerIsland = islandManagerImpl.of(activeBridges.get(player.getUniqueId()));
        removePlayerFromIsland(bridgerUserData, bridgerIsland);
        //TODO

    }

    private void removePlayerFromIsland(@NotNull BridgerUserData bridgerUserData, @NotNull BridgerIsland bridgerIsland){
        bridgerIsland.setResetting();
        bridgerUserData.userMatchCache().resetPlacedBlocks();
        bridgerUserData.userMatchCache().setPlayerAsLobby();
        activeBridges.remove(bridgerUserData.getPlayerUID());
        bridgerIsland.setIdle();
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

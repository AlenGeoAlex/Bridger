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

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class GameHandler {

    private final Bridger plugin;
    private final IslandManager islandManager;
    private final UserManager userManager;

    private final HashMap<UUID, String> activeBridges;

    public GameHandler(Bridger plugin) {
        this.plugin = plugin;
        this.islandManager = new IslandManager(plugin);
        this.userManager = new UserManager(plugin);
        this.activeBridges = new HashMap<>();
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

    public Optional<Island> toIsland(@NotNull Player player){
        final UserData userData = userManager.of(player.getUniqueId());
        if(userData == null) {
            plugin.getLogger().severe("Failed to get the data for user @"+getClass().getSimpleName()+"#toIsland(Player)");
            return Optional.empty();
        }
        Island island = null;
        Optional<Island> islandOptional = islandManager.getAnyFreeIsland(player);

        if(!islandOptional.isPresent()){
            plugin.messagingUtils().sendTo(player, userData.userSettings().getLanguage().asComponent(LangConfigurationPaths.NO_FREE_ISLANDS));
            return Optional.empty();
        }

        island = islandOptional.get();
        island.setOccupied();

        activeBridges.put(player.getUniqueId(), island.getIslandName());
        island.teleportToSpawn(player);
        plugin.messagingUtils().sendTo(player,
                userData.userSettings().getLanguage().asComponent(LangConfigurationPaths.TELEPORTED_TO_ISLAND,
                MessagePlaceholder.of("%island-name%", island.getIslandName())
                ));
        return Optional.of(island);
    }

    public void playerFirstBlock(@NotNull UserData userData){
        userData.userMatchCache().setPlayerAsPlaying();
        userData.userMatchCache().setStartTime(System.currentTimeMillis());

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

    public void playerCompleteGame(@NotNull Player player){
        if(!activeBridges.containsKey(player.getUniqueId()))
            return;

        final UserData userData = userManager.of(player.getUniqueId());
        if(userData == null) {
            plugin.getLogger().severe("Failed to get the data for user @"+getClass().getSimpleName()+"#playerRestartGame(Player)");
            return;
        }

        final Island island = islandManager.of(activeBridges.get(player.getUniqueId()));
        userData.userStats().addAsCompletedGame();
        userData.userMatchCache().setCurrentTime(System.currentTimeMillis() - userData.userMatchCache().getStartTime());

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

    public void playerQuitGame(Player player){
        if(!activeBridges.containsKey(player.getUniqueId()))
            return;

        final UserData userData = userManager.of(player.getUniqueId());
        if(userData == null) {
            plugin.getLogger().severe("Failed to get the data for user @"+getClass().getSimpleName()+"#playerQuitGame(Player)");
            return;
        }
        UserManager.handleLobbyTransport(player);

        plugin.messagingUtils().sendTo(player,
                userData.userSettings().getLanguage().asComponent(LangConfigurationPaths.PLAYER_QUIT_MATCH)
                );
        final Island island = islandManager.of(activeBridges.get(player.getUniqueId()));
        removePlayerFromIsland(userData, island);
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


}

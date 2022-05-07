package io.github.alenalex.bridger.manager;

import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.models.leaderboard.LeaderboardPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class LeaderboardManager {

    public static final int LEADERBOARD_RESET_DURATION = 60;

    public static boolean RELOADING = false;

    public static boolean isPopulating() {
        return RELOADING;
    }

    private final Bridger plugin;

    private final List<LeaderboardPlayer> leaderboardPlayers;

    private int counterTime;

    public LeaderboardManager(Bridger plugin) {
        this.plugin = plugin;
        this.leaderboardPlayers = new ArrayList<>(10);
        this.counterTime = 0;
    }

    public Bridger getPlugin() {
        return plugin;
    }


    public Runnable getCallable(){
        return new Runnable() {
            @Override
            public void run() {
                if(RELOADING)
                    return;

                if(counterTime >= LEADERBOARD_RESET_DURATION) {
                    populate();
                    counterTime=0;
                }
                else counterTime++;
            }
        };
    }

    private void populate(){
        RELOADING = true;
        this.plugin.dataProvider().getDatabaseProvider().getLeaderboardPlayers().thenAccept(new Consumer<List<LeaderboardPlayer>>() {
            @Override
            public void accept(List<LeaderboardPlayer> leaderboardPlayersList) {
                leaderboardPlayers.clear();
                leaderboardPlayers.addAll(leaderboardPlayersList);
            }
        });
        RELOADING = false;
    }

    public int nextResetIn(){
        return LEADERBOARD_RESET_DURATION - counterTime;
    }

    public LeaderboardPlayer ofPosition(int position){
        return leaderboardPlayers.get(position-1);
    }

    public boolean isInLeaderboard(@NotNull Player player){
        boolean found = false;
        for(LeaderboardPlayer leaderboardPlayer : leaderboardPlayers){
            if(leaderboardPlayer.getPlayerUID().equals(player.getUniqueId())) {
                found = true;
                break;
            }
        }
        return found;
    }
}

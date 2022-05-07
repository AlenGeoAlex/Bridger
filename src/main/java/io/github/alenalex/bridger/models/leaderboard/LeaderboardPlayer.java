package io.github.alenalex.bridger.models.leaderboard;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class LeaderboardPlayer implements Comparable<LeaderboardPlayer>{

    private final int pos;
    private final UUID playerUID;
    private final String playerName;
    private final long bestTime;

    public LeaderboardPlayer(int pos, UUID playerUID, String playerName, long bestTime) {
        this.pos = pos;
        this.playerUID = playerUID;
        this.playerName = playerName;
        this.bestTime = bestTime;
    }

    public int getPos() {
        return pos;
    }

    public UUID getPlayerUID() {
        return playerUID;
    }

    public String getPlayerName() {
        return playerName;
    }

    public long getBestTime() {
        return bestTime;
    }

    @Override
    public int compareTo(@NotNull LeaderboardPlayer o) {
        return Integer.compare(this.pos, o.getPos());
    }
}

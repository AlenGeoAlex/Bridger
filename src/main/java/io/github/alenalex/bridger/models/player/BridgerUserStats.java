package io.github.alenalex.bridger.models.player;

import com.google.common.base.Objects;
import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.api.models.player.UserStats;
import io.github.alenalex.bridger.utils.StringUtils;

public final class BridgerUserStats implements UserStats {

    public static BridgerUserStats DEFAULT;

    static {
        DEFAULT = new BridgerUserStats(0, 0, 0, 0);
    }


    private int wins;
    private int blocksPlaced;
    private int gamesPlayed;
    private long bestTime;

    public BridgerUserStats(int wins, int blocksPlaced, int gamesPlayed, long bestTime) {
        this.wins = wins;
        this.blocksPlaced = blocksPlaced;
        this.gamesPlayed = gamesPlayed;
        this.bestTime = bestTime;
    }

    @Override
    public int getWins() {
        return wins;
    }

    @Override
    public int getBlocksPlaced() {
        return blocksPlaced;
    }

    @Override
    public int getGamesPlayed() {
        return gamesPlayed;
    }

    @Override
    public long getBestTime() {
        return bestTime;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public void setBlocksPlaced(int blocksPlaced) {
        this.blocksPlaced = blocksPlaced;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public void setBestTime(long bestTime) {
        this.bestTime = bestTime;
    }

    public void addWin() {
        wins++;
    }

    public void addBlock() {
        blocksPlaced++;
    }

    public void addBlock(int blocksPlaced) {
        this.blocksPlaced += blocksPlaced;
    }

    public void negateBlock(int blocksPlaced) {
        this.blocksPlaced -= blocksPlaced;
    }

    public void addGame() {
        gamesPlayed++;
    }

    public void addAsCompletedGame(){
        addGame();
        addWin();
    }

    public String getBestTimeAsString(){
        return StringUtils.convertLongToReadableDate(bestTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BridgerUserStats bridgerUserStats = (BridgerUserStats) o;
        return wins == bridgerUserStats.wins && blocksPlaced == bridgerUserStats.blocksPlaced && gamesPlayed == bridgerUserStats.gamesPlayed && bestTime == bridgerUserStats.bestTime;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(wins, blocksPlaced, gamesPlayed, bestTime);
    }

    @Override
    public String toString() {
        return "UserStats{" +
                "wins=" + wins +
                ", blocksPlaced=" + blocksPlaced +
                ", gamesPlayed=" + gamesPlayed +
                ", bestTime=" + bestTime +
                '}';
    }

    public String asJson(){
        return Bridger.gsonInstance().toJson(this);
    }
}

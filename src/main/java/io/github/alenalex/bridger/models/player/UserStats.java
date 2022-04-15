package io.github.alenalex.bridger.models.player;

import com.google.common.base.Objects;

public final class UserStats {

    public static UserStats DEFAULT;

    static {
        DEFAULT = new UserStats(0, 0, 0, 0);
    }


    private int wins;
    private int blocksPlaced;
    private int gamesPlayed;
    private long bestTime;
    private long currentTime;

    public UserStats(int wins, int blocksPlaced, int gamesPlayed, long bestTime) {
        this.wins = wins;
        this.blocksPlaced = blocksPlaced;
        this.gamesPlayed = gamesPlayed;
        this.bestTime = bestTime;
        this.currentTime = 0;
    }

    public int getWins() {
        return wins;
    }

    public int getBlocksPlaced() {
        return blocksPlaced;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public long getBestTime() {
        return bestTime;
    }

    public long getCurrentTime() {
        return currentTime;
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

    public void setCurrentTime() {
        this.currentTime = System.currentTimeMillis();
    }

    public void setCurrentTimeAsBest(){
        bestTime = currentTime;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserStats userStats = (UserStats) o;
        return wins == userStats.wins && blocksPlaced == userStats.blocksPlaced && gamesPlayed == userStats.gamesPlayed && bestTime == userStats.bestTime && currentTime == userStats.currentTime;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(wins, blocksPlaced, gamesPlayed, bestTime, currentTime);
    }

    @Override
    public String toString() {
        return "UserStats{" +
                "wins=" + wins +
                ", blocksPlaced=" + blocksPlaced +
                ", gamesPlayed=" + gamesPlayed +
                ", bestTime=" + bestTime +
                ", currentTime=" + currentTime +
                '}';
    }
}

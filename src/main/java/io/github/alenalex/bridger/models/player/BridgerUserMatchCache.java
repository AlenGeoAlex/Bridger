package io.github.alenalex.bridger.models.player;

import fr.mrmicky.fastboard.FastBoard;
import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.api.models.player.UserMatchCache;
import io.github.alenalex.bridger.models.BridgerIsland;
import io.github.alenalex.bridger.utils.StringUtils;
import io.github.alenalex.bridger.workload.core.Workload;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class BridgerUserMatchCache implements UserMatchCache {
    private transient final BridgerUserData bridgerUserData;
    private final List<Block> placedBlocks;
    private long startTime;
    private long currentTime;
    private String spectatingIsland;
    private Status status;
    private FastBoard scoreBoard;

    public BridgerUserMatchCache(BridgerUserData bridgerUserData) {
        this.bridgerUserData = bridgerUserData;
        this.placedBlocks = new ArrayList<>();
        this.startTime = 0L;
        this.currentTime = 0L;
        this.spectatingIsland = null;
        this.status = Status.LOBBY;
        this.scoreBoard = new FastBoard(Objects.requireNonNull(bridgerUserData.getPlayer()));
        this.scoreBoard.updateTitle(Bridger.instance().configurationHandler().getScoreboardConfiguration().getLobbyConfig().getTranslatedTitle());
    }

    public FastBoard getScoreBoard() {
        return scoreBoard;
    }

    public enum Status {
        PLAYING,
        IDLE,
        LOBBY,
        SPECTATING
    }

    public void addBlockToCache(@NotNull Block block){
        block.setMetadata(BridgerIsland.PLACED_BLOCK, new FixedMetadataValue(Bridger.instance(), 0));
        this.placedBlocks.add(block);
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    public void resetPlacedBlocks(){
        if(placedBlocks.isEmpty())
            return;

        for(Block block: placedBlocks){
            try {
                Workload workload = () -> {
                    block.setType(Material.AIR);
                    if(block.hasMetadata(BridgerIsland.PLACED_BLOCK))
                        block.removeMetadata(BridgerIsland.PLACED_BLOCK, Bridger.instance());
                    if(bridgerUserData.userSettings().hasParticle()){

                    }
                };
                Bridger.instance().workloadHandler().getSyncThread().submit(workload);
            }catch (Exception ignored){}

        }
        bridgerUserData.userStats().addBlock(placedBlocks.size());
        placedBlocks.clear();
    }

    public void forceResetPlacedBlocks(){
        if(placedBlocks.isEmpty())
            return;

        for(Block block: placedBlocks){
            Workload workload = () -> {
                block.setType(Material.AIR);
                if(block.hasMetadata(BridgerIsland.PLACED_BLOCK))
                    block.removeMetadata(BridgerIsland.PLACED_BLOCK, Bridger.instance());
            };
            Bridger.instance().workloadHandler().getSyncThread().submit(workload);
        }
        bridgerUserData.userStats().addBlock(placedBlocks.size());
        placedBlocks.clear();
    }

    public String getSpectatingIsland() {
        return spectatingIsland;
    }

    @Override
    public Optional<String> getOptionalSpectatingIsland(){
        return Optional.ofNullable(spectatingIsland);
    }

    public void setSpectatingIsland(String spectatingIsland) {
        this.status = Status.SPECTATING;
        this.spectatingIsland = spectatingIsland;
        this.scoreBoard.updateTitle(Bridger.instance().configurationHandler().getScoreboardConfiguration().getSpectateConfig().getTranslatedTitle());
    }

    public void clearSpectatingIsland(){
        setSpectatingIsland(null);
        this.status = Status.LOBBY;
        this.scoreBoard.updateTitle(Bridger.instance().configurationHandler().getScoreboardConfiguration().getLobbyConfig().getTranslatedTitle());
    }

    @Override
    public boolean isUserSpectating(){
        return status == Status.SPECTATING;
    }

    @Override
    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    @Override
    public long getCurrentTime() {
        return currentTime;
    }

    @Override
    public String getCurrentTimeAsString(){
        return StringUtils.convertLongToReadableDate(currentTime);
    }

    public void setPlayerAsIdle(){
        status = Status.IDLE;
        this.scoreBoard.updateTitle(Bridger.instance().configurationHandler().getScoreboardConfiguration().getMatchConfig().getTranslatedTitle());
    }

    public void setPlayerAsPlaying(){
        setStartTime(System.currentTimeMillis());
        status = Status.PLAYING;
    }

    public void setPlayerAsLobby(){
        status = Status.LOBBY;
        this.scoreBoard.updateTitle(Bridger.instance().configurationHandler().getScoreboardConfiguration().getLobbyConfig().getTranslatedTitle());
    }

    public Status getStatus() {
        return status;
    }

    public String asJson(){
        return Bridger.gsonInstance().toJson(this);
    }

    public int getBlocksPlaced(){
        return placedBlocks.size();
    }

    public void deleteScoreboard(){
        this.scoreBoard.delete();
        this.scoreBoard = null;
    }

    public void spawnScoreboard(){
        this.scoreBoard = new FastBoard(Objects.requireNonNull(bridgerUserData.getPlayer()));
        if(status == Status.LOBBY)
            this.scoreBoard.updateTitle(Bridger.instance().configurationHandler().getScoreboardConfiguration().getLobbyConfig().getTranslatedTitle());
        else if(status == Status.SPECTATING)
            this.scoreBoard.updateTitle(Bridger.instance().configurationHandler().getScoreboardConfiguration().getSpectateConfig().getTranslatedTitle());
        else this.scoreBoard.updateTitle(Bridger.instance().configurationHandler().getScoreboardConfiguration().getMatchConfig().getTranslatedTitle());
    }

    @Override
    public boolean isUserOnLobby() {
        return status == Status.LOBBY;
    }

    @Override
    public boolean isUserIdle() {
        return status == Status.IDLE;
    }

    @Override
    public boolean isUserOnMatch() {
        return status == Status.PLAYING;
    }
}

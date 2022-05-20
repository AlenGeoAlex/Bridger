package io.github.alenalex.bridger.models.player;

import fr.mrmicky.fastboard.FastBoard;
import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.models.Island;
import io.github.alenalex.bridger.utils.StringUtils;
import io.github.alenalex.bridger.scheduler.core.Workload;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;
import xyz.xenondevs.particle.task.TaskManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserMatchCache {
    private transient final UserData userData;
    private final List<Block> placedBlocks;
    private long startTime;
    private long currentTime;
    private String spectatingIsland;
    private Status status;
    private FastBoard scoreBoard;

    public UserMatchCache(UserData userData) {
        this.userData = userData;
        this.placedBlocks = new ArrayList<>();
        this.startTime = 0L;
        this.currentTime = 0L;
        this.spectatingIsland = null;
        this.status = Status.LOBBY;
        this.scoreBoard = new FastBoard(Objects.requireNonNull(userData.getPlayer()));
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
        block.setMetadata(Island.PLACED_BLOCK, new FixedMetadataValue(Bridger.instance(), 0));
        this.placedBlocks.add(block);
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    public void resetPlacedBlocks(){
        if(placedBlocks.isEmpty())
            return;

        final ParticleEffect effect = userData.userSettings().getParticle().orElse(null);

        for(Block block: placedBlocks){
            try {
                Workload workload = () -> {
                    block.setType(Material.AIR);
                    if(block.hasMetadata(Island.PLACED_BLOCK))
                        block.removeMetadata(Island.PLACED_BLOCK, Bridger.instance());

                    if(effect != null)
                         new ParticleBuilder(effect, block.getLocation()).display(block.getWorld().getPlayers());

                };
                Bridger.instance().workloadHandler().getSyncThread().submit(workload);
            }catch (Exception ignored){}

        }

        userData.userStats().addBlock(placedBlocks.size());
        placedBlocks.clear();
    }

    public void forceResetPlacedBlocks(){
        if(placedBlocks.isEmpty())
            return;

        for(Block block: placedBlocks){
            Workload workload = () -> {
                block.setType(Material.AIR);
                if(block.hasMetadata(Island.PLACED_BLOCK))
                    block.removeMetadata(Island.PLACED_BLOCK, Bridger.instance());
            };
            Bridger.instance().workloadHandler().getSyncThread().submit(workload);
        }
        userData.userStats().addBlock(placedBlocks.size());
        placedBlocks.clear();
    }

    public String getSpectatingIsland() {
        return spectatingIsland;
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

    public boolean isUserSpectating(){
        return status == Status.SPECTATING;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getCurrentTime() {
        return currentTime;
    }

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
        this.scoreBoard = new FastBoard(Objects.requireNonNull(userData.getPlayer()));
        if(status == Status.LOBBY)
            this.scoreBoard.updateTitle(Bridger.instance().configurationHandler().getScoreboardConfiguration().getLobbyConfig().getTranslatedTitle());
        else if(status == Status.SPECTATING)
            this.scoreBoard.updateTitle(Bridger.instance().configurationHandler().getScoreboardConfiguration().getSpectateConfig().getTranslatedTitle());
        else this.scoreBoard.updateTitle(Bridger.instance().configurationHandler().getScoreboardConfiguration().getMatchConfig().getTranslatedTitle());
    }
}

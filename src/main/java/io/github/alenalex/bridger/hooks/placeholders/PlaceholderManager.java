package io.github.alenalex.bridger.hooks.placeholders;

import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.models.player.BridgerUserData;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaceholderManager extends PlaceholderExpansion {

    private final Bridger plugin;

    public PlaceholderManager(Bridger plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "bridger";
    }

    @Override
    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().get(0);
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if(params.equals("player-wins")){
            BridgerUserData data = plugin.gameHandler().userManager().of(player.getUniqueId());
            if(data != null){
                return String.valueOf(data.userStats().getWins());
            }else return null;
        }

        if(params.equals("player-games-played")){
            BridgerUserData data = plugin.gameHandler().userManager().of(player.getUniqueId());
            if(data != null){
                return String.valueOf(data.userStats().getGamesPlayed());
            }else return null;
        }

        if(params.equals("player-blocks-placed")){
            BridgerUserData data = plugin.gameHandler().userManager().of(player.getUniqueId());
            if(data != null){
                return String.valueOf(data.userStats().getBlocksPlaced());
            }else return null;
        }

        if(params.equals("player-best-time")){
            BridgerUserData data = plugin.gameHandler().userManager().of(player.getUniqueId());
            if(data != null){
                return data.userStats().getBestTimeAsString();
            }else return null;
        }

        if(params.equals("isplaying")){
            return String.valueOf(plugin.gameHandler().isPlayerPlaying(player));
        }

        if(params.equals("player-island")){
            return String.valueOf(plugin.gameHandler().getIslandOfPlayer(player).orElse(null));
        }

        return null;
    }
}

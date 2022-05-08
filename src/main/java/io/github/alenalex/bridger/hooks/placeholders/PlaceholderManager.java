package io.github.alenalex.bridger.hooks.placeholders;

import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.handler.LeaderboardHandler;
import io.github.alenalex.bridger.models.leaderboard.LeaderboardPlayer;
import io.github.alenalex.bridger.models.player.UserData;
import io.github.alenalex.bridger.utils.adventure.MessageFormatter;
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
            UserData data = plugin.gameHandler().userManager().of(player.getUniqueId());
            if(data != null){
                return String.valueOf(data.userStats().getWins());
            }else return null;
        }

        if(params.equals("player-games-played")){
            UserData data = plugin.gameHandler().userManager().of(player.getUniqueId());
            if(data != null){
                return String.valueOf(data.userStats().getGamesPlayed());
            }else return null;
        }

        if(params.equals("player-blocks-placed")){
            UserData data = plugin.gameHandler().userManager().of(player.getUniqueId());
            if(data != null){
                return String.valueOf(data.userStats().getBlocksPlaced());
            }else return null;
        }

        if(params.equals("player-best-time")){
            UserData data = plugin.gameHandler().userManager().of(player.getUniqueId());
            if(data != null){
                return data.userStats().getBestTimeAsString();
            }else return null;
        }

        if(params.equals("is_playing")){
            return String.valueOf(plugin.gameHandler().isPlayerPlaying(player));
        }

        if(params.equals("player-island")){
            return String.valueOf(plugin.gameHandler().getIslandOfPlayer(player).orElse(null));
        }

        if(params.equals("leaderboard_reset_in")){
            return String.valueOf(plugin.leaderboardManager().nextResetIn());
        }

        if(params.equals("leaderboard_reset")){
            return String.valueOf(LeaderboardHandler.LEADERBOARD_RESET_DURATION);
        }

        if(params.startsWith("leaderboard_position")){
            String[] splitArgs = params.split("_");
            if(splitArgs.length != 3) return null;
            int position = 0;
            try {
                 position = Integer.parseInt(splitArgs[2]);
            }catch (Exception ignored){
                plugin.getLogger().warning("The args provided placeholder leaderboard_position_[pos] is not a number. Provided - "+params);
                return null;
            }

            if(position <= 0 || position >= 11) return MessageFormatter.colorizeLegacy("&cInvalid Position");


            final LeaderboardPlayer playerData = plugin.leaderboardManager().ofPosition(position);
            if(playerData == null)
                return MessageFormatter.colorizeLegacy("&aNil");

            return playerData.getPlayerName();
        }

        return null;
    }
}

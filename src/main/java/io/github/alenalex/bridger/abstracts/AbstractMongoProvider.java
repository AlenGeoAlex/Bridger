package io.github.alenalex.bridger.abstracts;

import io.github.alenalex.bridger.models.player.UserCosmetics;
import io.github.alenalex.bridger.models.player.UserData;
import io.github.alenalex.bridger.models.player.UserSettings;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public abstract class AbstractMongoProvider {

    public CompletableFuture<Document> serializeUserData(@NotNull UserData userData){
        return CompletableFuture.supplyAsync(new Supplier<Document>() {
            @Override
            public Document get() {
                final Document document;

                document = new Document("uuid", userData.getPlayerUID())
                        .append("wins",userData.userStats().getWins())
                        .append("blocks_placed", userData.userStats().getBlocksPlaced())
                        .append("games_played", userData.userStats().getGamesPlayed())
                        .append("best_time", userData.userStats().getBestTime())
                        .append("language", userData.userSettings().getLanguageAsString())
                        .append("particle",userData.userSettings().getParticleAsString())
                        .append("material",userData.userSettings().getMaterialAsString())
                        .append("firework",userData.userSettings().getFireWorkAsString())
                        .append("scoreboard",userData.userSettings().isScoreboardEnabled())
                        .append("fireworks",userData.userCosmetics().getFireWorkUnlocked())
                        .append("particles", userData.userCosmetics().getParticleUnlocked())
                        .append("materials", userData.userCosmetics().getMaterialUnlocked());

                return document;
            }
        });
    }

    public CompletableFuture<UserData> deserializeUser(@NotNull Document document){
        return CompletableFuture.supplyAsync(new Supplier<UserData>() {
            @Override
            public UserData get() {
                try {
                    final List<String> fireWorkEnabled = document.getList("fireworks", String.class);
                    final List<String> particlesEnabled = document.getList("particles", String.class);
                    final List<String> materialsEnabled = document.getList("materials", String.class);

                    final UserCosmetics userCosmetics = new UserCosmetics(fireWorkEnabled, materialsEnabled, particlesEnabled);

                }catch (Exception e){
                    e.printStackTrace();
                    return null;
                }

                return null;
            }
        });
    }

}

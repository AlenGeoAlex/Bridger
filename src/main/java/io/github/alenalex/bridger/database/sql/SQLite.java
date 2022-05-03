package io.github.alenalex.bridger.database.sql;

import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.abstracts.AbstractSQL;
import io.github.alenalex.bridger.interfaces.IDatabaseProvider;
import io.github.alenalex.bridger.models.player.BridgerUserCosmetics;
import io.github.alenalex.bridger.models.player.BridgerUserData;
import io.github.alenalex.bridger.models.player.BridgerUserSettings;
import io.github.alenalex.bridger.models.player.BridgerUserStats;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class SQLite extends AbstractSQL implements IDatabaseProvider {

    public static final String SQLITE_DRIVER = "org.sqlite.JDBC";

    public SQLite(Bridger plugin) {
        super(plugin);
    }

    @Override
    public List<String> getPrepareDatabaseQuery() {
        return new ArrayList<String>(){{
            //Table -> Player data like blocks placed, games played, etc.
            add("CREATE TABLE IF NOT EXISTS bridger_user " +
                    "(`uid` VARCHAR(40) NOT NULL PRIMARY KEY, " +
                    "`wins` INTEGER DEFAULT '0' NOT NULL, " +
                    "`blocks_placed` INTEGER DEFAULT '0' NOT NULL, " +
                    "`games_played` INTEGER DEFAULT '0' NOT NULL, " +
                    "`best_time` BIGINT DEFAULT '0' NOT NULL) ");

            //Table -> Player settings like language particles, material, scoreboard, etc.
            add("CREATE TABLE IF NOT EXISTS bridger_us_settings " +
                    "(`uid` VARCHAR(40) NOT NULL PRIMARY KEY, " +
                    "`language` VARCHAR(40) NOT NULL , " +
                    "`particle` VARCHAR(40) , " +
                    "`material` VARCHAR(40) , " +
                    "`firework` VARCHAR(40) ," +
                    "`scoreboard` BOOLEAN NOT NULL) "
                    );

            //Table -> Unlockables
            add("CREATE TABLE IF NOT EXISTS bridger_user_cosmetics " +
                    "(`uid` VARCHAR(40) NOT NULL PRIMARY KEY, " +
                    "`fireworks` TEXT , " +
                    "`particles` TEXT , " +
                    "`materials` TEXT" + ")");
        }};
    }

    @Override
    public boolean connect() {
        try {
            Class.forName(SQLITE_DRIVER);
        } catch (ClassNotFoundException e) {
            getPlugin().getLogger().severe("Failed to load SQLite driver!");
            e.printStackTrace();
            return false;
        }
        return connect("sqlite", "bridger.db");
    }

    @Override
    public boolean isConnectionOpen() {
        return isConnected();
    }

    @Override
    public boolean prepareDatabase() {
        return createDatabase();
    }

    @Override @Nullable
    public CompletableFuture<BridgerUserData> loadOrRegisterUser(@NotNull UUID uuid) {
        return CompletableFuture.supplyAsync(new Supplier<BridgerUserData>() {
            @Override
            public BridgerUserData get() {

                if(!isConnected()) return null;

                try (final PreparedStatement userSettings = connection.prepareStatement("SELECT * FROM bridger_us_settings WHERE uid = ?")) {
                    try (final PreparedStatement userData = connection.prepareStatement("SELECT * FROM bridger_user WHERE uid = ?")) {
                        try (final PreparedStatement userCos = connection.prepareStatement("SELECT * FROM bridger_user_cosmetics WHERE uid = ?")) {


                            userSettings.setString(1, uuid.toString());
                            //Load user settings
                            final ResultSet settingsSet = userSettings.executeQuery();

                            BridgerUserSettings settings = null;
                            if (settingsSet != null) {

                                if (settingsSet.next()) {
                                    settings = new BridgerUserSettings(
                                            settingsSet.getString("language"),
                                            settingsSet.getString("particle"),
                                            settingsSet.getString("material"),
                                            settingsSet.getString("firework"),
                                            settingsSet.getBoolean("scoreboard")
                                    );
                                }
                                settingsSet.close();
                            }

                            if (settings == null) {

                                settings = BridgerUserSettings.DEFAULT;
                                //This should mean that the user has never played before. So registering him to database
                                final PreparedStatement insertSettings = connection.prepareStatement("INSERT INTO bridger_us_settings (uid, language, particle, material, firework ,scoreboard) VALUES (?, ?, ?, ?, ?, ?)");
                                insertSettings.setString(1, uuid.toString());
                                insertSettings.setString(2, settings.getLanguageAsString());
                                insertSettings.setString(3, settings.getParticleAsString());
                                insertSettings.setString(4, settings.getMaterialAsString());
                                insertSettings.setString(5, settings.getFireWorkAsString());
                                insertSettings.setBoolean(6, settings.isScoreboardEnabled());

                                insertSettings.executeUpdate();
                                insertSettings.close();
                            }
                            userSettings.close();

                            //Load user data
                            userData.setString(1, uuid.toString());
                            final ResultSet dataSet = userData.executeQuery();
                            BridgerUserStats stats = null;
                            if (dataSet != null) {
                                if (dataSet.next()) {
                                    stats = new BridgerUserStats(
                                            dataSet.getInt("wins"),
                                            dataSet.getInt("blocks_placed"),
                                            dataSet.getInt("games_played"),
                                            dataSet.getLong("best_time")
                                    );
                                }
                                dataSet.close();
                            }
                            if (stats == null) {
                                //This should mean that the user has never played before. So registering him to database
                                final PreparedStatement insertData = connection.prepareStatement("INSERT INTO bridger_user (uid, wins, blocks_placed, games_played, best_time) VALUES (?, ?, ?, ?, ?)");
                                insertData.setString(1, uuid.toString());
                                insertData.setInt(2, 0);
                                insertData.setInt(3, 0);
                                insertData.setInt(4, 0);
                                insertData.setLong(5, 0);
                                insertData.executeUpdate();

                                insertData.close();
                                stats = BridgerUserStats.DEFAULT;
                            }
                            userData.close();

                            //Load user cosmetics
                            BridgerUserCosmetics cosmetics = null;
                            userCos.setString(1, uuid.toString());
                            final ResultSet cosmeticsSet = userCos.executeQuery();
                            if (cosmeticsSet != null) {
                                if (cosmeticsSet.next()) {
                                    cosmetics = new BridgerUserCosmetics(
                                            BridgerUserCosmetics.deserialize(cosmeticsSet.getString("fireworks")),
                                            BridgerUserCosmetics.deserialize(cosmeticsSet.getString("particles")),
                                            BridgerUserCosmetics.deserialize(cosmeticsSet.getString("materials"))
                                    );
                                }
                                cosmeticsSet.close();

                            }

                            if (cosmetics == null) {
                                final PreparedStatement insertCosmetics = connection.prepareStatement("INSERT INTO bridger_user_cosmetics (uid, fireworks, particles, materials) VALUES (?, ?, ?, ?)");
                                insertCosmetics.setString(1, uuid.toString());
                                insertCosmetics.setString(2, BridgerUserCosmetics.serialize(BridgerUserCosmetics.DEFAULT.getFireWorkUnlocked()));
                                insertCosmetics.setString(3, BridgerUserCosmetics.serialize(BridgerUserCosmetics.DEFAULT.getParticleUnlocked()));
                                insertCosmetics.setString(4, BridgerUserCosmetics.serialize(BridgerUserCosmetics.DEFAULT.getMaterialUnlocked()));

                                insertCosmetics.executeUpdate();
                                insertCosmetics.close();
                                cosmetics = BridgerUserCosmetics.DEFAULT;
                            }

                            userCos.close();
                            return new BridgerUserData(uuid, stats, settings, cosmetics);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        });
    }

    @Override
    public void saveUserAsync(@NotNull BridgerUserData user) {
        getPlugin().getServer().getScheduler().runTaskAsynchronously(getPlugin(), new Runnable() {
            @Override
            public void run() {
                try(final PreparedStatement updateSettings = connection.prepareStatement("UPDATE bridger_us_settings SET language = ?, particle = ?, material = ?, scoreboard = ? WHERE uid = ?")) {

                    updateSettings.setString(1, user.userSettings().getLanguageAsString());
                    updateSettings.setString(2, user.userSettings().getParticleAsString());
                    updateSettings.setString(3, user.userSettings().getMaterialAsString());
                    updateSettings.setBoolean(4, user.userSettings().isScoreboardEnabled());
                    updateSettings.setString(5, user.getPlayerUID().toString());

                    updateSettings.executeUpdate();
                    updateSettings.close();
                try (final PreparedStatement updateData = connection.prepareStatement("UPDATE bridger_user SET wins = ?, blocks_placed = ?, games_played = ?, best_time = ? WHERE uid = ?");){

                    updateData.setInt(1, user.userStats().getWins());
                    updateData.setInt(2, user.userStats().getBlocksPlaced());
                    updateData.setInt(3, user.userStats().getGamesPlayed());
                    updateData.setLong(4, user.userStats().getBestTime());
                    updateData.setString(5, user.getPlayerUID().toString());

                    updateData.executeUpdate();
                    updateData.close();
                }

                try(final PreparedStatement updateCosmetics = connection.prepareStatement("UPDATE bridger_user_cosmetics SET fireworks = ?, particles = ?, materials = ? WHERE uid = ?");){

                    updateCosmetics.setString(1, BridgerUserCosmetics.serialize(user.userCosmetics().getFireWorkUnlocked()));
                    updateCosmetics.setString(2, BridgerUserCosmetics.serialize(user.userCosmetics().getParticleUnlocked()));
                    updateCosmetics.setString(3, BridgerUserCosmetics.serialize(user.userCosmetics().getMaterialUnlocked()));
                    updateCosmetics.setString(4, user.getPlayerUID().toString());

                    updateCosmetics.executeUpdate();
                    updateCosmetics.close();

                }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void saveAllUserSync(@NotNull List<BridgerUserData> users) {
        try(
                final PreparedStatement updateSettings = connection.prepareStatement("UPDATE bridger_us_settings SET language = ?, particle = ?, material = ?, scoreboard = ? WHERE uid = ?;");
                final PreparedStatement updateData = connection.prepareStatement("UPDATE bridger_user SET wins = ?, blocks_placed = ?, games_played = ?, best_time = ? WHERE uid = ?;");
                final PreparedStatement cosmeticUpdate = connection.prepareStatement("UPDATE bridger_user_cosmetics SET fireworks = ?, particles = ?, materials = ? WHERE uid = ?;")
        )  {


            for (BridgerUserData user : users) {
                updateSettings.setString(1, user.userSettings().getLanguageAsString());
                updateSettings.setString(2, user.userSettings().getParticleAsString());
                updateSettings.setString(3, user.userSettings().getMaterialAsString());
                updateSettings.setBoolean(4, user.userSettings().isScoreboardEnabled());
                updateSettings.setString(5, user.getPlayerUID().toString());

                updateSettings.executeUpdate();

                updateData.setInt(1, user.userStats().getWins());
                updateData.setInt(2, user.userStats().getBlocksPlaced());
                updateData.setInt(3, user.userStats().getGamesPlayed());
                updateData.setLong(4, user.userStats().getBestTime());
                updateData.setString(5, user.getPlayerUID().toString());

                updateData.executeUpdate();

                cosmeticUpdate.setString(1, BridgerUserCosmetics.serialize(user.userCosmetics().getFireWorkUnlocked()));
                cosmeticUpdate.setString(2, BridgerUserCosmetics.serialize(user.userCosmetics().getParticleUnlocked()));
                cosmeticUpdate.setString(3, BridgerUserCosmetics.serialize(user.userCosmetics().getMaterialUnlocked()));
                cosmeticUpdate.setString(4, user.getPlayerUID().toString());

                cosmeticUpdate.executeUpdate();
            }

            updateSettings.close();
            updateData.close();
            cosmeticUpdate.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void saveAllUsersAsync(@NotNull List<BridgerUserData> users) {
        getPlugin().getServer().getScheduler().runTaskAsynchronously(getPlugin(), new Runnable() {
            @Override
            public void run() {
                try(
                    final PreparedStatement updateSettings = connection.prepareStatement("UPDATE bridger_us_settings SET language = ?, particle = ?, material = ?, scoreboard = ? WHERE uid = ?;");
                    final PreparedStatement updateData = connection.prepareStatement("UPDATE bridger_user SET wins = ?, blocks_placed = ?, games_played = ?, best_time = ? WHERE uid = ?;");
                    final PreparedStatement cosmeticUpdate = connection.prepareStatement("UPDATE bridger_user_cosmetics SET fireworks = ?, particles = ?, materials = ? WHERE uid = ?;")
                    ) {



                    for (BridgerUserData user : users) {
                        updateSettings.setString(1, user.userSettings().getLanguageAsString());
                        updateSettings.setString(2, user.userSettings().getParticleAsString());
                        updateSettings.setString(3, user.userSettings().getMaterialAsString());
                        updateSettings.setBoolean(4, user.userSettings().isScoreboardEnabled());
                        updateSettings.setString(5, user.getPlayerUID().toString());

                        updateSettings.executeUpdate();

                        updateData.setInt(1, user.userStats().getWins());
                        updateData.setInt(2, user.userStats().getBlocksPlaced());
                        updateData.setInt(3, user.userStats().getGamesPlayed());
                        updateData.setLong(4, user.userStats().getBestTime());
                        updateData.setString(5, user.getPlayerUID().toString());

                        updateData.executeUpdate();

                        cosmeticUpdate.setString(1, BridgerUserCosmetics.serialize(user.userCosmetics().getFireWorkUnlocked()));
                        cosmeticUpdate.setString(2, BridgerUserCosmetics.serialize(user.userCosmetics().getParticleUnlocked()));
                        cosmeticUpdate.setString(3, BridgerUserCosmetics.serialize(user.userCosmetics().getMaterialUnlocked()));
                        cosmeticUpdate.setString(4, user.getPlayerUID().toString());

                        cosmeticUpdate.executeUpdate();
                    }

                    updateSettings.close();
                    updateData.close();
                    cosmeticUpdate.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

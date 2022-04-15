package io.github.alenalex.bridger.database.sql;

import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.abstracts.AbstractSQL;
import io.github.alenalex.bridger.interfaces.IDatabaseProvider;
import io.github.alenalex.bridger.models.player.UserData;
import io.github.alenalex.bridger.models.player.UserSettings;
import io.github.alenalex.bridger.models.player.UserStats;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class SQLite extends AbstractSQL implements IDatabaseProvider {

    public SQLite(Bridger plugin) {
        super(plugin);
    }

    @Override
    public List<String> getPrepareDatabaseQuery() {
        return new ArrayList<String>(){{
            //Table -> Player data like blocks placed, games played, etc.
            add("CREATE TABLE IF NOT EXISTS bridger_user " +
                    "`uid` VARCHAR(40) NOT NULL PRIMARY KEY, " +
                    "`wins` INTEGER DEFAULT '0' NOT NULL, " +
                    "`blocks_placed` INTEGER DEFAULT '0' NOT NULL, " +
                    "`games_played` INTEGER DEFAULT '0' NOT NULL, " +
                    "`best_time` BIGINT DEFAULT '0' NOT NULL, ");

            //Table -> Player settings like language particles, material, scoreboard, etc.
            add("CREATE TABLE IF NOT EXISTS bridger_us_settings " +
                    "`uid` VARCHAR(40) NOT NULL, " +
                    "`language` VARCHAR(40) DEFAULT 'en' NOT NULL , " +
                    "`particle` VARCHAR(40) , " +
                    "`material` VARCHAR(40) , " +
                    "`scoreboard` BOOLEAN NOT NULL, "
                    );
        }};
    }

    @Override
    public boolean connect() {
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

    @Override
    public CompletableFuture<UserData> loadOrRegisterUser(@NotNull UUID uuid) {
        return CompletableFuture.supplyAsync(new Supplier<UserData>() {
            @Override
            public UserData get() {
                try (final PreparedStatement userSettings = connection.prepareStatement("SELECT * FROM bridger_us_settings WHERE uid = ?")) {
                    try (final PreparedStatement userData = connection.prepareStatement("SELECT * FROM bridger_user WHERE uid = ?")) {

                        //Load user settings
                        final ResultSet settingsSet = userSettings.executeQuery();
                        UserSettings settings = null;
                        if (settingsSet != null && settingsSet.next()) {
                            settings = new UserSettings(
                                    settingsSet.getString("language"),
                                    settingsSet.getString("particle"),
                                    settingsSet.getString("material"),
                                    settingsSet.getBoolean("scoreboard")
                            );
                            settingsSet.close();
                        }
                        if (settings == null) {
                            settings = UserSettings.DEFAULT;

                            final PreparedStatement insertSettings = connection.prepareStatement("INSERT INTO bridger_us_settings (uid, language, particle, material, scoreboard) VALUES (?, ?, ?, ?, ?)");
                            insertSettings.setString(1, uuid.toString());
                            insertSettings.setString(2, settings.getLanguageAsString());
                            insertSettings.setString(3, settings.getParticleAsString());
                            insertSettings.setString(4, settings.getMaterialAsString());
                            insertSettings.setBoolean(5, settings.isScoreboardEnabled());

                            insertSettings.executeUpdate();
                            insertSettings.close();
                        }
                        userSettings.close();

                        //Load user data
                        final ResultSet dataSet = userData.executeQuery();
                        UserStats stats = null;
                        if (dataSet != null && dataSet.next()) {
                            stats = new UserStats(
                                    dataSet.getInt("wins"),
                                    dataSet.getInt("blocks_placed"),
                                    dataSet.getInt("games_played"),
                                    dataSet.getLong("best_time")
                            );
                            dataSet.close();
                        }
                        if (dataSet == null) {
                            final PreparedStatement insertData = connection.prepareStatement("INSERT INTO bridger_user (uid, wins, blocks_placed, games_played, best_time) VALUES (?, ?, ?, ?, ?)");
                            insertData.setString(1, uuid.toString());
                            insertData.setInt(2, 0);
                            insertData.setInt(3, 0);
                            insertData.setInt(4, 0);
                            insertData.setLong(5, 0);
                            insertData.executeUpdate();

                            insertData.close();
                            stats = UserStats.DEFAULT;
                        }
                        userData.close();

                        return new UserData(uuid, stats, settings);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        });
    }

    @Override
    public void saveUserAsync(@NotNull UserData user) {
        getPlugin().getServer().getScheduler().runTaskAsynchronously(getPlugin(), new Runnable() {
            @Override
            public void run() {
                try {
                    final PreparedStatement updateSettings = connection.prepareStatement("UPDATE bridger_us_settings SET language = ?, particle = ?, material = ?, scoreboard = ? WHERE uid = ?");
                    updateSettings.setString(1, user.userSettings().getLanguageAsString());
                    updateSettings.setString(2, user.userSettings().getParticleAsString());
                    updateSettings.setString(3, user.userSettings().getMaterialAsString());
                    updateSettings.setBoolean(4, user.userSettings().isScoreboardEnabled());
                    updateSettings.setString(5, user.getPlayerUID().toString());

                    updateSettings.executeUpdate();
                    updateSettings.close();

                    final PreparedStatement updateData = connection.prepareStatement("UPDATE bridger_user SET wins = ?, blocks_placed = ?, games_played = ?, best_time = ? WHERE uid = ?");
                    updateData.setInt(1, user.userStats().getWins());
                    updateData.setInt(2, user.userStats().getBlocksPlaced());
                    updateData.setInt(3, user.userStats().getGamesPlayed());
                    updateData.setLong(4, user.userStats().getBestTime());
                    updateData.setString(5, user.userStats().toString());

                    updateData.executeUpdate();
                    updateData.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void saveAllUsersAsync(@NotNull List<UserData> users) {
        getPlugin().getServer().getScheduler().runTaskAsynchronously(getPlugin(), new Runnable() {
            @Override
            public void run() {
                try {
                    final PreparedStatement updateSettings = connection.prepareStatement("UPDATE bridger_us_settings SET language = ?, particle = ?, material = ?, scoreboard = ? WHERE uid = ?");
                    final PreparedStatement updateData = connection.prepareStatement("UPDATE bridger_user SET wins = ?, blocks_placed = ?, games_played = ?, best_time = ? WHERE uid = ?");

                    for (UserData user : users) {
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
                        updateData.setString(5, user.userStats().toString());

                        updateData.executeUpdate();
                    }

                    updateSettings.close();
                    updateData.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

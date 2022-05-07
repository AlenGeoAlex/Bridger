package io.github.alenalex.bridger.database.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.database.config.ConnectionConfig;
import io.github.alenalex.bridger.interfaces.IDatabaseProvider;
import io.github.alenalex.bridger.models.leaderboard.LeaderboardPlayer;
import io.github.alenalex.bridger.models.player.UserData;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

//TODO
public class MongoDB implements IDatabaseProvider{

    private final Bridger plugin;
    private MongoClient mongoClient = null;
    private MongoDatabase mongoDatabase = null;
    private final MongoConnectionListener connectionListener;

    public MongoDB(Bridger plugin) {
        this.plugin = plugin;
        this.connectionListener = new MongoConnectionListener();
    }

    @Override
    public boolean connect() {
        try {
            final MongoClientOptions clientOptions = new MongoClientOptions.Builder()
                    .addServerMonitorListener(this.connectionListener)
                    .socketTimeout(3000)
                    .maxWaitTime(5000)
                    .connectTimeout(5000)
                    .build();

            final ConnectionConfig connectionConfig = ConnectionConfig.of(plugin.configurationHandler().getConfigurationFile().getSectionOf("storage"));
            mongoClient = new MongoClient(new ServerAddress(connectionConfig.getHost(), connectionConfig.getPort()));
            final MongoCredential mongoCredential = MongoCredential.createCredential(connectionConfig.getUsername(), connectionConfig.getDatabase(), connectionConfig.getPassword().toCharArray());
            this.mongoDatabase = mongoClient.getDatabase(connectionConfig.getDatabase());

            return connectionListener.isConnectionOpen();
        }catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isConnectionOpen() {
        return connectionListener.isConnectionOpen();
    }

    @Override
    public void closeConnection() {
        try {
            if(mongoClient != null)
                mongoClient.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean prepareDatabase() {
        return false;
    }

    @Override
    public CompletableFuture<UserData> loadOrRegisterUser(@NotNull UUID uuid) {
        return null;
    }

    @Override
    public void saveAllUserSync(@NotNull List<UserData> users) {

    }

    @Override
    public void saveUserAsync(@NotNull UserData user) {

    }

    @Override
    public void saveAllUsersAsync(@NotNull List<UserData> users) {

    }

    @Override
    public String providerName() {
        return "MongoDB";
    }

    @Override
    public CompletableFuture<List<LeaderboardPlayer>> getLeaderboardPlayers() {
        return null;
    }


}

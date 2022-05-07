package io.github.alenalex.bridger.database;

import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.database.mongo.MongoDB;
import io.github.alenalex.bridger.database.sql.MySQL;
import io.github.alenalex.bridger.database.sql.SQLite;
import io.github.alenalex.bridger.interfaces.IDatabaseProvider;

public class DataProvider {

    private final Bridger plugin;

    private IDatabaseProvider databaseProvider;

    public DataProvider(Bridger plugin) {
        this.plugin = plugin;
    }

    public boolean initConnection() {
        switch (plugin.configurationHandler().getConfigurationFile().getStorageType().toUpperCase()) {

            case "MYSQL":
                databaseProvider = new MySQL(plugin);
                break;
            case "SQLITE":
                databaseProvider = new SQLite(plugin);
                break;
            case "MONGODB": case "MONGO":
                databaseProvider = new MongoDB(plugin);
                break;
            default:
                plugin.getLogger().severe("You have not specified a valid storage type in the config.yml file. Please specify one of the following: MYSQL, SQLITE");
                return false;
        }

        return databaseProvider.connect();
    }

    public void closeConnection() {
        if(databaseProvider != null)
            databaseProvider.closeConnection();
    }

    public IDatabaseProvider getDatabaseProvider() {
        return databaseProvider;
    }
}

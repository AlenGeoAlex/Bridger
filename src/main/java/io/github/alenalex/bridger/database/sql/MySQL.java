package io.github.alenalex.bridger.database.sql;

import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.abstracts.AbstractSQL;
import io.github.alenalex.bridger.database.sql.config.ConnectionConfig;
import io.github.alenalex.bridger.interfaces.IDatabaseProvider;
import io.github.alenalex.bridger.models.player.UserData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;


public class MySQL extends AbstractSQL implements  IDatabaseProvider {

    public static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";

    public MySQL(Bridger plugin) {
        super(plugin);
    }

    @Override
    public List<String> getPrepareDatabaseQuery() {
        return new ArrayList<String>(){{

        }};
    }

    @Override
    public boolean connect() {
        try {
            Class.forName(MYSQL_DRIVER);
        } catch (ClassNotFoundException e) {
            getPlugin().getLogger().severe("MySQL driver not found!");
            e.printStackTrace();
            return false;
        }
        return super.connect("mysql",
                ConnectionConfig.of(getPlugin().configurationHandler().getConfigurationFile().getSectionOf("storage"))
                );
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
}

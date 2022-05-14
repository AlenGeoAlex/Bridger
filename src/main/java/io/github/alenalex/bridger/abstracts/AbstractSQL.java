package io.github.alenalex.bridger.abstracts;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.database.config.ConnectionConfig;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public abstract class AbstractSQL {
    protected static final String JDBC_REMOTE_URL = "jdbc:"
            + "%s" //Type of DB (postgresql,mysql)
            + "://"
            + "%s" // host
            + ":"
            + "%d" // port
            + "/"
            + "%s" // database
            + "?autoReconnect="
            + "%s" // auto reconnect
            + "&"
            + "useSSL="
            + "%s" // use ssl
            ;
    protected static final String JDBC_LOCAL_URL = "jdbc:" +
            "%s" +//Type of DB (sqlite, h2, hsqldb)
            ":" +
            "%s" // File path of the database
            ;

    private final Bridger plugin;
    protected Connection connection;

    public AbstractSQL(Bridger plugin) {
        this.plugin = plugin;
    }

    public boolean connect(@NotNull String driver, @NotNull String fileName) {
        try {
            final File parentFolder = new File(plugin.getDataFolder().getPath()+File.separator+"database");

            if(!parentFolder.exists())
                parentFolder.mkdirs();

            this.connection = DriverManager.getConnection(
                    String.format(
                            JDBC_LOCAL_URL,
                            driver,
                            plugin.getDataFolder().getPath()+ File.separator+"database"+File.separator+fileName
                    )
            );
            return isConnected();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean createDatabase(){
        if(!isConnected()){
            this.plugin.getLogger().severe("The plugin was unable to connect to the database!");
            return false;
        }

        final List<String> queries = getPrepareDatabaseQuery();

        boolean completed = true;

        for(String query : queries){
            if(StringUtils.isBlank(query)){
                getPlugin().getLogger().warning("Located an empty query while creating database, contact the developer! "+getClass().getName()+".");
                continue;
            }

            try(final PreparedStatement ps = connection.prepareStatement(query)) {
                ps.execute();
                ps.close();
            }catch (Exception e){
                e.printStackTrace();
                completed = false;
                break;
            }
        }

        if(!completed)
            getPlugin().getLogger().severe("The plugin was forced to abort creating database, Checkout the errors above and report it to the developer!");

        return completed;
    }

    public abstract List<String> getPrepareDatabaseQuery();

    public boolean connect(@NotNull String driver, @NotNull ConnectionConfig connectionConfig) {
        try {
            final HikariConfig config = connectionConfig.asHikariConfig();
            config.setJdbcUrl(
                    String.format(
                            JDBC_REMOTE_URL,
                            driver,
                            connectionConfig.getHost(),
                            connectionConfig.getPort(),
                            connectionConfig.getDatabase(),
                            connectionConfig.isAutoReconnect(),
                            connectionConfig.useSSL()
                    )
            );
            config.setUsername(connectionConfig.getUsername());
            config.setPassword(connectionConfig.getPassword());

            this.connection = new HikariDataSource(config).getConnection();
            return isConnected();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean isConnected() {
        if(connection == null)
            return false;

        try {
            return !connection.isClosed();
        } catch (SQLException ignored) {
            return false;
        }
    }

    public void closeConnection() {
        if(connection == null)
            return;

        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Bridger getPlugin() {
        return plugin;
    }
}
